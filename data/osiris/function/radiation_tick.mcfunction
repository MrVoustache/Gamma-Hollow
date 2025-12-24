# 1. Clear the 'has_timer' tag from everyone first
tag @e remove has_timer

# 2. Add the tag to anyone who actually has a score recorded
tag @e[scores={radiation_timer=..2147483647}] add has_timer

# 3. If someone is exposed and DOES NOT have the tag, give them 0 so they can be hit
execute as @e[predicate=osiris:exposed_to_cosmic_rays, tag=!has_timer] run scoreboard players set @s radiation_timer 0

# 4. The logic from before: hit those at 0 and start timer
execute as @e[predicate=osiris:exposed_to_cosmic_rays, scores={radiation_timer=..0}] unless entity @s[type=player, gamemode=creative] unless entity @s[type=player, gamemode=spectator] at @s run function osiris:trigger_damage

# 5. Countdown
execute as @e[scores={radiation_timer=1..}] run scoreboard players remove @s radiation_timer 1