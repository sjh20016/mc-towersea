# 检测塔海维度内玩家是否坠落
execute as @a if dimension towersea:towersea at @s if predicate towersea:below_threshold run function towersea:safety/fall_return_check
