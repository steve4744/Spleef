# Spleef_reloaded

[![Resource](https://img.shields.io/badge/SpigotMC-Resource-orange.svg)](https://www.spigotmc.org/resources/spleef_reloaded-spleef-for-1-13-1-20.53359/)
[![Java CI with Maven](https://github.com/steve4744/Spleef/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/steve4744/Spleef/actions?query=workflow%3A%22Java+CI+with+Maven%22)
[![Dev Build](https://img.shields.io/badge/Dev%20Build-Latest-orange?logo=github-actions)](https://github.com/steve4744/Spleef/releases)
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)


[![Discord Chat](https://img.shields.io/discord/308323056592486420?logo=discord)](https://discord.gg/wFYSAS4)
[![bStats](https://img.shields.io/badge/statistics-bstats-brightgreen.svg)](https://bstats.org/plugin/bukkit/Spleef_reloaded)
[![Crowdin](https://badges.crowdin.net/spleefreloaded/localized.svg)](https://crowdin.com/project/spleefreloaded)
[![PayPal](https://img.shields.io/badge/paypal-donate-yellow?logo=paypal)](https://www.paypal.com/paypalme/steve4744)


## Description

Spleef is a highly configurable, fully automated minigame for Minecraft servers. Traditionally players start on a layer of snow, and once the game starts players attempt to break the blocks below other players. If this causes them to fall, they lose the game and become a spectator for the remainder of the game. The last player remaining wins the game.

The latest version of Spleef_reloaded requires a minimum Java version of 17, and is supported on servers running Minecraft versions 1.20.1+.

The following description and features apply to the latest release. Some of the features will not be present in the older versions which are maintained and supported on an 'as is' basis, and will only be updated if a bug is reported or an existing feature breaks.

The plugin features a customisable shop where players can buy items such as weapons, armour, double-jumps, splash potions, snowballs (with knockback) and commands which run when the game starts. There is an option to enable PVP in an arena, assign kits, and the plugin also interfaces with HeadsPlus (by ThatsMusic99) allowing players to buy/wear custom heads during the game.

Optionally, a fee can be set to join each arena, which can be monetary or any Minecraft item such gold_nuggets. Rewards for winning the game can be set to any combination of coins, materials, XP or a command based reward. Scoreboards, leaderboards, placeholders and holograms are fully supported (see the Dependencies section below).


## Download

If your server is running Minecraft 1.20.1+, then the latest version of Spleef\_reloaded can be [downloaded from Spigot.](https://www.spigotmc.org/resources/spleef_reloaded.xxxxx/ "Spleef_reloaded")


## Development Builds

Development snapshots are created by GitHub Actions every time a commit is pushed to the most recent snapshot branch. The latest snapshot build can be downloaded from [GitHub Releases.](https://github.com/steve4744/Spleef/releases "Releases")


## Features

    Supports multiple arenas
    Automatic arena regeneration
    Custom Events
    Native Party system
    Support for AlessioDP Parties
    Configurable block destroy delay
    Force-start voting system
    Permission controlled force-start command
    Join fee can be set per arena
    Arena currency (money or any Minecraft material)
    Arena selection GUI
    Configurable anti-camping system
    Translatable messages
    Command whitelist
    Formatting codes support
    Full tab completion based on permissions
    Signs
    Configurable per-arena time limit
    Configurable per-arena countdown
    Configurable sounds
    In-game scoreboard
    Titles and bossbars
    Spectator system
    Player tracker for spectators
    Player stats
    Leader board
    Auto updating leader board signs
    Arena leave checker
    Customizable shop
    Shop can be enabled/disabled per arena
    Kits - can be enabled per arena
    Heads - interfaces with HeadsPlus plugin by Thatsmusic99
    PVP can be enabled/disabled per arena
    Configurable rewards for players finishing in any position: 1st, 2nd, 3rd, ... etc.
    Built-in placeholder support
    mcMMO support - allow players in same mcMMO party to PVP if enabled in arena
    MySQL support
    Bungeecord support


## Dependencies

The following plugin dependencies are needed to compile the source code. All are optional to run Spleef_reloaded on a Spigot server.
Links to download each plugin are available on Spleef_reloaded's Spigot page.

The latest version of Spleef_reloaded has been tested with the following versions of these plugins:

    WorldEdit 7.3.2 (optional, internal commands setP1 and setP2 can be used to set arena bounds)
    Vault 1.7 (optional, required to use economy)
    HeadsPlus 7.1.2 (optional, allow players to buy and run around wearing different heads)
    mcMMO 2.2.012 (optional, will allow players in same mcMMO party to PVP in arena)
    PlaceholderAPI 2.11.6 (optional, needed to use placeholders)
    AlessioDP Parties 3.2.13 (optional, can be used in place of native spleef parties)
    
One of the following plugins (or similar) is required to create Holographic Leaderboards for Spleef_reloaded (see wiki for details and example).
    
    DecentHolograms 2.8.8 (optional, an example plugin needed to create holograms)
    HolographicDisplays 3.0.1 (optional, an example plugin needed to create holograms)

If you are interested in recording the amount of time players spend playing Spleef_reloaded, with the option to reward players in-game for surviving a set amount of time (includes its own leaderboards and placeholders):

    SpleefTimedRewards (optional add-on for recording time played in arena, and rewarding survival time)

FAWE is also supported, and can be used in place of the WorldEdit on 1.13+ servers.



<br />
<br />
<br />
Updated steve4744 - 15th June 2024
