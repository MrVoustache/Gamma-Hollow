# # This line will run even if the macro lines fail, helping us debug
# tellraw @a "--- Macro Function Attempted ---"

# # 1. DEBUG: Print context and storage value to chat
# # Using current_damage to match what we stored
# $tellraw @a ["",{"text":"[DEBUG] ","color":"red"},{"text":"Target: ","color":"gray"},{"selector":"@s","color":"yellow"},{"text":" | Damage Value: ","color":"gray"},{"text":"$(current_damage)","color":"gold"}]

# Visual/Sound feedback (No $ needed here as there are no variables)
effect give @s minecraft:blindness 1 1 true
effect give @s minecraft:glowing 1 1 true
# playsound minecraft:entity.illusioner.cast_spell ambient @a ~ ~ ~ 1 2
particle minecraft:flash ~ ~1.5 ~ 0.1 0.1 0.1 0 1
# 2. The Macro command
# Fixed: Changed damage_val to current_damage
$damage @s $(current_damage) minecraft:generic