scoreboard players set @a towersea_cd 0
scoreboard players set @a towersea_y 0
execute at @a run function towersea:safety/tick
execute at @a run function towersea:portal/tick
execute at @a run function towersea:magic/bridge/bridge_tick
execute at @a run function towersea:magic/common/tick
