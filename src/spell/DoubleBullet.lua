local spellData = {}

local wing = 8
local wingAngle = 20
local stack = 5

spellData.onStart = function()
	boss:setMaxHP(50)
	spell:startTimer()
	spell:startCounting()
end

local Lasseree = function()
	return spawnBullet(
		sequence(
			var("jndex", get("j")),
			var("kndex", get("k")),
			setCostume("OvalBullet"),
			setSize(15),
			setColor(45),
			jsif( equal( mod(get("jndex"), 2), 1 ),
				function() return setColor(140) end
			),
			setBrightness(-30),
			changeBrightness( mul(get("kndex"), 15) ),
			addCircleHitbox("bulletHB", 7),
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
					wait( sub(180, mul(get("kndex"), 22)) ),
					tween("ghost", 100, 30),
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
					playSound("shot"),
					jsfor("j", 1, wing, function()
						return jsfor("k", 1, stack, function()
							return Lasseree()
						end)
					end)
				),

				sequence(
					wait(240),
					tween("ghost", 100, 30),
					destroy()
				)
			)
		)
	)
end

spellData.buildAction = function()

return sequence(
	sound("jingle", "[TH] Jingle"),
	setSoundVolume("jingle", -0.25),
	playSound("jingle"),

	sound("shot", "[TH] Shot"),
	setSoundVolume("shot", -15.5),
	forever("sequence",
		playSound("shot"),
		jsfor("i", 1, 1, function()
			return CasualWalk()
		end),
		wait(60)
	)
)

end

return spellData