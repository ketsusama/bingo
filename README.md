# Bingo

![link](https://img.shields.io/badge/API-<1.17>-blue) ![link](https://img.shields.io/badge/Version-1.17.1-blue) ![link](https://img.shields.io/badge/Minecraft-1.17-brightgreen)

## Description

On Minecraft, the goal of Bingo is to be the first to retrieve a number of items from a given array of items at the
beginning of the game to each of the players. The game takes place in survival and the players generally share the same
card without PvP.

### Permissions

| Permission | Description                |
|:-----------|:---------------------------|
| `bingo.*`  | Executes the /game command |

### Commandes

| Commande                   | Description                              | Paramètres                                                                   |
|:---------------------------|:-----------------------------------------|:-----------------------------------------------------------------------------|
| `/game start`              | Force the launch of the current instance | -                                                                            |
| `/game stop <instance_id>` | Stop the current the instance by index   | `instance_id` : The instance index in the list                                            |

## Configurations

**File Settings** \
playersRequiredToStart : *Number of people required to launch the game* \
sizeOfBeamArea : *Radius of the teleportation zone* \
gameGlobalPermission : *Global permissions of the plugin* \
lineToComplete : *Number of lines to complete to win a game* \
banItemStack : *List of banned items for bingo card*

## Dépendances

- [Lombok](https://projectlombok.org/) v1.18.22
- [Jackson](https://github.com/FasterXML/jackson) multiple version per modules
