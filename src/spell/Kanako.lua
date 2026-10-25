local spellData = {}

local circleCount = 5
local density = 48

spellData.configure = {
	name = "Replicated [Mountain of Faith]",
	timer = 60*120,
	playerCandidateRadius = 70,
	countableTime = timer,
	isSpell = true,
	caster = "LAMBDA"
}

spellData.onStart = function()
	boss:setMaxHP(100)
	spell:startTimer()
	spell:startCounting()
end

local function spawnCircle(amount, radius, centerDistance)
	return jsfor("p", 1, amount, function()
		return spawnBullet( sequence(
			replicate("p", "j", "i"),
			var("group", roundDown( div(get("i"), 4 ) ) ),
			setGhost(100),
			setColor(45),
			changeColor( mul(-45, get("j")) ),
			setCostume("BulletFog"),
			addCircleHitbox("bulletHB", 5),
			addHitboxTag("bulletHB", "ENEMY_BULLET", "CLEARABLE"),
			disableHitbox("bulletHB"),

			parallel(
				sequence(
					-- Position this ring's center.
					warp(boss),
					look(90),
					turn(mul(get("i"), 360 / circleCount)),
					forward(centerDistance),

					-- Arrange this ring's bullets.
					turn(180),
					turn(mul(360 / amount, get("p"))),
					forward( mul( get("j"), radius ) ),

					wait( mul(0.75, get("p")) ),
					playSound("shot"),
					tween("size", 30, 18,
						"brightness", 100, 0,
						"ghost", 100, 0, 30),
					setCostume("BulletRinged"),
					enableHitbox("bulletHB"),
					var("speed", 3),
					wait( mul( sub( 3, get("j") ) , 105 ) ),
					sequence(
						turn( add(
							mul( get("group"), -25 ),
							125
						), 120),
						forever("sequence",
							forward( get("speed") ),
							jsif(greater(get("speed"), -5),
								function()
									return change("speed", -0.075)
								end
							),
							wait()
						)
					)
				),

				sequence(
					wait(1200),
					disableHitbox("bulletHB"),
					tween("ghost", 0, 100, 30),
					destroy()
				)
			)
		) )
	end)
end

spellData.buildAction = function()
	return sequence(
		sound("jingle", "[TH] Jingle", -5.25),
		sound("shot", "[TH] Shot", -32.5),

		forever("sequence",
			playSound("jingle"),

			jsfor("j", 1, 3, function()
				return sequence(
					jsfor("i", 0, circleCount-1, function()
						return spawnCircle(density, 50, 100)
					end),
					wait(30)
				)
			end),
			wait(200)
		)
	)
end

return spellData
