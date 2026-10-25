local spellData = {}

spellData.configure = {
	name = "Replicated [Pebbles That Become Boulders]",
	playerCandidateRadius = 40,
	timer = 3600,
	countableTime = 3600,
	isSpell = true,
	caster = "LAMBDA"
}

spellData.onStart = function()
	boss:setMaxHP(75)
	spell:startTimer()
	spell:startCounting()
end

spellData.buildAction = function()
	local count = 800
	local step = 0.65
	local waitIteration = 0.125
	local goldenAngle = 360 * (1 - 2 / (1 + math.sqrt(5)))

	local function bullete()
		local capSpeed = -3.5
		return spawnBullet(sequence(
			replicate("i"),
			var("speed", 0.30),

			setGhost(100),
			setCostume("BulletOval"),
			addCircleHitbox("bulletHB", 4),
			addHitboxTag("bulletHB", "ENEMY_BULLET", "CLEARABLE"),
			disableHitbox("bulletHB"),

			goTo(boss),
			look(get("offset")),
			turn(mul(get("i"), goldenAngle)),
			forward(mul(get("i"), step)),
			wait(add(mul(get("i"), waitIteration), 1)),
			jsif(less(distance(player), 100),
				function()
					return destroy()
				end
			),

			parallel(
				sequence(
					playSound("shot"),
					forever("sequence",
						forward(get("speed")),
						changeDesaturation(-0.1),
						changeBrightness(-0.1),
						wait()
					)
				),
				tween("color", 240, 130, 180),
				sequence(
					tween("size", 30, 9,
						"brightness", 100, 60,
						"ghost", 90, 0, 45),
					enableHitbox("bulletHB"),
					var("aa", random(1, 360)),
					turn(get("aa"), 60),
					jswhile(greater(get("speed"), capSpeed), function()
						return sequence(
							change("speed", -0.035),
							wait()
						)
					end)
				),
				sequence(
					wait(600),
					disableHitbox("bulletHB"),
					tween("ghost", 100, 60),
					destroy()
				)
			)
		))
	end

	return sequence(
		var("offset", mul(random(), 360)),

		sound("jingle", "[TH] Jingle", -0.25),
		sound("shot", "[TH] Shot", -17.5),

		forever("sequence",
			playSound("jingle"),
			jsfor("i", 1, count, function()
				return bullete()
			end),
			wait(300)
		)
	)
end

return spellData
