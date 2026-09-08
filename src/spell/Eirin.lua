local spell = {}

local rings = 16
local density = 32
local initialSpeed = 1.25
local angle1, angle2 = 360/rings, 360/density
local cooldown = 175

spell.onStart = function()
	boss:setMaxHP(50)
end

local bullette = function()
	return spawnBullet(
		sequence(
			setCostume("OvalBullet"),
			setColor(130),
			setSize(10),
			setBrightness(-100),
			setGhost(70),
			addCircleHitbox("bulletHB", 5),
			addHitboxTag("bulletHB", "ENEMY_BULLET"),
			var("index", get("i")),
			var("jndex", get("j")),
			var("speed", initialSpeed),
			warp(999, 999),

			parallel(
				sequence(
					--wait( div(get("jndex")) ),
					warp(boss),
					look( get( "offset" ) ),
					turn(mul(get("index"), angle1)),
					forward(110),

					look(0),
					turn(mul(get("jndex"), angle2)),
					forward(30)

				),
				
				forever("sequence",
					forward(get("speed")),
					changeBrightness(1),
					changeGhost(-1)
				),
				
				sequence(
					wait(30),
					turn( mul( get("dir"), 45) ),
					forever("sequence",
						change("speed", -0.025)
					)
				),

				sequence(
					wait(360),
					destroy()
				)
			)
		)
	)
end

spell.buildAction = function()

return sequence(
	var("offset", mul(random(), 360)),
	var("count", 0),
	var("dir", mod(get("count", 2)) ),

	sound("jingle", "[TH] Jingle"),
	setsoundvolume("jingle", -0.25),

	sound("shot", "[TH] Shot"),
	setsoundvolume("shot", -15.5),

	forever("sequence",
		playsound("jingle"),
		playsound("shot"),
		set("dir", sub( mul(get("count"), 2), 1 )),
		jsfor("i", 1, rings, function()
			return jsfor("j", 1, density, function()
				return bullette()
			end)
		end),
		change("count", 1),
		wait(cooldown)
	)
)

end

return spell