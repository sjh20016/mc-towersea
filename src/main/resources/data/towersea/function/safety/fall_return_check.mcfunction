# 随机选择返回点
scoreboard players set #return towersea_rng 0
scoreboard players add #return towersea_rng 4
execute if score #return towersea_rng matches 1 run function towersea:safety/return_point_1
execute if score #return towersea_rng matches 2 unless score #return towersea_rng matches 3 run function towersea:safety/return_point_2
execute if score #return towersea_rng matches 3 unless score #return towersea_rng matches 4 run function towersea:safety/return_point_3
execute if score #return towersea_rng matches 4 run function towersea:safety/return_point_4
