local spellData = {}

local count = 270
local step = 2
local waitIteration = 0.2
local goldenAngle = 360 * (1 - 2 / (1 + math.sqrt(5)))

spellData.configure = {
	name = "[Lua] Replication Sign \"Digitalized Pebbles\"",
	timer = 3600,
	playerCandidateRadius = 40,
	countableTime = timer,
	isSpell = true,
	caster = "LAMBDA"
}

spellData.onStart = function()
	boss:setMaxHP(50)
	spell:startTimer()
	spell:startCounting()
end

local bullete = function()
	local capSpeed = -7.5

	return spawnBullet(
		sequence(
			var("index", get("i")),
			var("speed", 0.30),

			setGhost(100),
			setCostume("BulletOval"),
			addCircleHitbox("bulletHB", 5),
			addHitboxTag("bulletHB", "ENEMY_BULLET"),
			addHitboxTag("bulletHB", "CLEARABLE"),
			disableHitbox("bulletHB"),

			wait(
				add(
					mul(get("index"), waitIteration),
					1
				)
			),

			parallel(
				sequence(
					goTo(boss),
					look(get("offset")),
					turn(mul(get("index"), goldenAngle)),
					forward(mul(get("index"), step)),
					playSound("shot"),

					forever("sequence",
						forward(get("speed")),
						wait()
					)
				),
				tween("color", 240, 130, 90),
				sequence(
					tween("size", 30, 11, "brightness", 100, 60, "ghost", 90, 0, 45),
					enableHitbox("bulletHB"),
					jswhile(
						greater(get("speed"), capSpeed),
						function()
							return sequence(
								change("speed", -0.07),
								wait()
							)
						end
					)
				),

				sequence(
					wait(480),
					disableHitbox("bulletHB"),
					tween("ghost", 100, 60),
					destroy()
				)
			)
		)
	)

end

spellData.buildAction = function()
	return sequence(
	var("offset", mul(random(), 360)),

		sound("jingle", "[TH] Jingle"),
		setSoundVolume("jingle", -0.25),

		sound("shot", "[TH] Shot"),
		setSoundVolume("shot", -17.5),

		forever("sequence",
			playSound("jingle"),

			jsfor("i", 1, count, function()
				return bullete()
			end),

			wait(150)
		)
	)
end

return spellData