local spellData = {}

local circleCount = 5
local density = 100

spellData.configure = {
	name = "Replicated [Mountain of Faith]",
	timer = 60*120,
	playerCandidateRadius = 70,
	countableTime = timer,
	isSpell = false,
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
			replicate("p", "i", "j"),
			setGhost(100),
			setColor(45),
			changeColor( mul(-45, get("j")) ),
			setCostume("BulletCircle"),
			addCircleHitbox("bulletHB", 7),
			addHitboxTag("bulletHB", "ENEMY_BULLET"),
			addHitboxTag("bulletHB", "CLEARABLE"),
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

					wait( mul(0.5, get("p")) ),
					playSound("shot"),
					tween("size", 30, 13,
						"brightness", 100, 0,
						"ghost", 100, 0, 30),
					enableHitbox("bulletHB")
				),

				sequence(
					wait(300),
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
		sound("jingle", "[TH] Jingle", -0.25),
		sound("shot", "[TH] Shot", -22.5),

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
			wait(450)
		)
	)
end

return spellData