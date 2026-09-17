local spell = {}

local wing = 8
local wingAngle = 20
local stack = 4

spell.onStart = function()
	boss:setMaxHP(50)
end

local Lasseree = function()
	return spawnBullet(
		sequence(
			var("jndex", get("j")),
			var("kndex", get("k")),
			setCostume("OvalBullet"),
			setSize(15),
			setColor( add( mul(95, mod(get("jndex"), 2) ), 45 ) ),
			setBrightness( -20 ),
			changeBrightness( mul(get("kndex"), 10) ),
			addCircleHitbox("bulletHB", 12),
			addHitboxTag("bulletHB", "ENEMY_BULLET"),
			parallel(
				sequence(
					warp(get("rep1x"), get("rep1y")),
					lookTowards(player),
					turn( mul(wingAngle, sub(get("jndex"), (wing+1)/2) ) ),

					forever("sequence",
						forward( add( mul(get("kndex"), 2.5), 3.5) )
					)
				),

				sequence(
					wait(180),
					destroy()
				)
			)
		)
	)
end

local CasualWalk = function()
	return spawnBullet(
		sequence(
			var("speed", div(random(40,60),10)),
			var("rep1x", 0),
			var("rep1y", 0),
			setCostume("CircleBullet"),
			setSize(40),
			setBrightness(40),
			setGhost(40),
			warp(999,999),

			parallel(
				sequence(
					warp(boss),
					turn( random(0, 360) ),

					forever("sequence",
						changeColor(1),
						forward(2.5),
						set("rep1x", get("x")),
						set("rep1y", get("y"))
					)
				),

				forever("sequence",
					wait(10),
					playsound("shot"),
					jsfor("j", 1, wing, function()
						return jsfor("k", 1, stack, function()
							return Lasseree()
						end)
					end)
				),

				sequence(
					wait(240),
					destroy()
				)
			)
		)
	)
end

spell.buildAction = function()

return sequence(
	sound("jingle", "[TH] Jingle"),
	setsoundvolume("jingle", -0.25),
	playsound("jingle"),

	sound("shot", "[TH] Shot"),
	setsoundvolume("shot", -15.5),
	forever("sequence",
		playsound("shot"),
		jsfor("i", 1, 1, function()
			return CasualWalk()
		end),
		wait(60)
	)
)

end

return spell