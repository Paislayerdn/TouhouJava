local spellData = {}

local rings = 17
local density = 29
local initialSpeed = 1.25
local angle1, angle2 = 360/rings, 360/density
local cooldown = 175

spellData.config = {
	name = "Eirin",
	timer = 120*60,
	playerCandidateRadius = 35,
	isSpell = false,
	caster = "LAMBDA"
}

spellData.onStart = function()
	boss:setMaxHP(50)
	spell:startTimer()
	spell:startCounting()
end

local bullette = function()
	return spawnBullet(
		sequence(
			var("index", get("i")),
			var("jndex", get("j")),
			var("speed", initialSpeed),
			var("colorOffset", add( 120, get("jndex") )  ),
			setCostume("OvalBullet"),
			addCircleHitbox("bulletHB", 5),
			addHitboxTag("bulletHB", "ENEMY_BULLET"),
			addHitboxTag("bulletHB", "CLEARABLE"),
			setColor( random(40, 210) ),
			warp(999, 999),

			parallel(
				sequence(
					warp(boss),
					look( get( "offset" ) ),
					turn(mul(get("index"), angle1)),
					forward(110),

					look(0),
					turn(mul(get("jndex"), angle2)),
					forward(30),
					forever("sequence",
						forward(get("speed"))
					)
				),
				tween("size", 20, 9.5,
					"color", get("colorOffset"),
					"brightness", 100, 10,
					"ghost", 100, 0,
					add(70, mul(get("index"), 5) ,mul(get("jndex"), 1.5)), easing.quadInOut),		
				sequence(
					wait(1),
					turn( mul( get("dir"), 45), 29 ),
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

spellData.buildAction = function()

return sequence(
	var("offset", mul(random(), 360)),
	var("dir", -1),

	sound("jingle", "[TH] Jingle"),
	setSoundVolume("jingle", -0.25),

	sound("shot", "[TH] Shot"),
	setSoundVolume("shot", -15.5),

	forever("sequence",
		playSound("jingle"),
		playSound("shot"),
		set("dir", mul(get("dir"), -1) ),
		jsfor("i", 1, rings, function()
			return jsfor("j", 1, density, function()
				return bullette()
			end)
		end),
		wait(cooldown)
	)
)

end

return spellData