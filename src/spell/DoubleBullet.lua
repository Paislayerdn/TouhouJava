local spellData = {}

local wing = 8
local wingAngle = 20
local stack = 5

spellData.configure = {
	name = "DoubleBullet",
	timer = 60*60,
	playerCandidateRadius = 40,
	isSpell = false,
	caster = "LAMBDA"
}

spellData.onStart = function()
	boss:setMaxHP(50)
	spell:startTimer()
end

local Lasseree = function()
	return spawnBullet( sequence(
		replicate("j", "k"),

		setSize(15),
		setColor(45),
		setCostume("BulletOval"),
		jsif(equal(mod(get("j"), 2), 1),
			function()
				return setColor(140)
			end
		),
		setBrightness(-30),
		changeBrightness( mul(get("k"), 15) ),
		addCircleHitbox("bulletHB", 7),
		addHitboxTag("bulletHB", "ENEMY_BULLET"),
		var("localSpeed", add( mul(get("k"), 3.5), 4)),

		parallel(
			sequence(
				warp(get("rep1x"), get("rep1y")),
				lookTowards(player),
				turn( mul(wingAngle, sub(get("j"), (wing+1)/2) ) ),

				forever("sequence",
					forward( get("localSpeed") ),
					wait()
				)
			),

			sequence(
				wait( sub(180, mul(get("k"), 22)) ),
				tween("ghost", 100, 30),
				destroy()
			)
		)
	))
end

local CasualWalk = function()
	return spawnBullet( sequence(
		var("speed", div(random(40,60),10)),
		var("rep1x", 0),
		var("rep1y", 0),

		setSize(40),
		setBrightness(40),
		setGhost(40),
		setCostume("BulletStar"),
		addCircleHitbox("nothing", 7),

		parallel(
			sequence(
				warp(boss),
				turn( random(0, 360) ),

				forever("sequence",
					changeColor(1),
					forward( get("speed") ),
					set("rep1x", get("x")),
					set("rep1y", get("y")),
					wait()
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
	) )
end

spellData.buildAction = function()
	return sequence(
		sound("jingle", "[TH] Jingle", -0.25),
		sound("shot", "[TH] Shot", -15.5),

		playSound("jingle"),
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
