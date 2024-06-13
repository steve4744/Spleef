/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package spleef.conversation;

import static org.bukkit.ChatColor.*;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import spleef.Spleef;
import spleef.arena.Arena;

public class SpleefConversation implements ConversationAbandonedListener {
	private ConversationFactory conversationFactory;

    private Player player;
    private Spleef plugin;
    private Arena arena;

    public SpleefConversation(Spleef plugin, Player player, Arena arena, ConversationType conversationType){
        this.player = player;
        this.plugin = plugin;
        this.arena = arena;

        conversationFactory = new ConversationFactory(this.plugin)
                .withEscapeSequence("cancel")
                .withTimeout(30)
                .thatExcludesNonPlayersWithMessage("This is only possible in-game, sorry.")
                .addConversationAbandonedListener(this)
                .withFirstPrompt(getEntryPrompt(conversationType, player));
    }

    private Prompt getEntryPrompt(ConversationType type, Player player) {
        player.sendMessage(GRAY + "Enter 'cancel' anytime to quit the conversation.");
        switch (type){
            case ARENAPRIZE:
                return new ArenaRewardConversation(arena);
            default:
                player.sendMessage(GRAY + "[" + GOLD + "Spleef" + GRAY + "]" + RED + " Unexpected conversation type: " + type);
                return null;
        }
    }

    @Override
    public void conversationAbandoned(ConversationAbandonedEvent event) {
        if (!event.gracefulExit())
            event.getContext().getForWhom().sendRawMessage(GRAY + "[" + GOLD + "Spleef" + GRAY + "]" + RED + "Conversation aborted...");
    }

    public static void sendErrorMessage(ConversationContext context, String message) {
        context.getForWhom().sendRawMessage(GRAY + "[" + GOLD + "Spleef" + GRAY + "] " + RED + message + ". Please try again...");
    }

    public void begin() {
        Conversation convo = conversationFactory.buildConversation(player);
        convo.getContext().setSessionData("playerName", player.getName());
        convo.begin();
    }

}
