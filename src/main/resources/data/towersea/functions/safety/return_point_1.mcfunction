execute in minecraft:overworld run tp @s 0 360 0
effect give @s minecraft:slow_falling 20 0 true
effect give @s minecraft:resistance 8 0 true
effect give @s minecraft:darkness 4 0 true
tellraw @s {"text":"[TowerSea] 从塔海深处坠落，安全返回","color":"gold"}
