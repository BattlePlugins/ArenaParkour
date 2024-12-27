# ArenaParkour

A parkour plugin using [BattleArena](https://github.com/BattlePlugins/BattleArena)

ArenaParkour is a parkour plugin using BattleArena, allowing you to create your own parkour courses with configurable checkpoints. Supports creating parkour minigames, or integrating into existing areas (i.e. lobby parkours).

## Parkour Templates

ArenaParkour includes a couple templates for pre-made parkour games. These are the parkour minigame, which act as a standard minigame for parkour, and the lobby parkour which is less of a minigame but sits in an existing lobby.

For examples configurations of these modes, check out the [templates](https://github.com/BattlePlugins/ArenaParkour/tree/master/templates) folder.

## Documentation
Full documentation for ArenaParkour can be found on the [BattleDocs](https://docs.battleplugins.org/books/additional-gamemodes/chapter/parkour) website.

## Commands
| Command                                  | Description                                      |
|------------------------------------------|--------------------------------------------------|
| /parkour checkpoint add <map>            | Adds a checkpoint to a parkour arena.            |
| /parkour checkpoint remove <map> <index> | Removes a checkpoint from a parkour arena.       |
| /parkour checkpoint clear <map>          | Clears all the checkpoints from a parkour arena. |
| /parkour checkpoint index <from> <to>    | Changes the index of a checkpoint.               |
| /parkour checkpoint list <map>           | Lists all the checkpoints in a parkour arena.    |

## Permissions
| Permission                                    | Command                    |
|-----------------------------------------------|----------------------------|
| battlearena.command.parkour.checkpoint.add    | /parkour checkpoint add    |
| battlearena.command.parkour.checkpoint.remove | /parkour checkpoint remove |
| battlearena.command.parkour.checkpoint.clear  | /parkour checkpoint clear  |
| battlearena.command.parkour.checkpoint.index  | /parkour checkpoint index  |
| battlearena.command.parkour.checkpoint.list   | /parkour checkpoint list   |
