local spellData = {}

local count1 = 27
local stack1 = 7
local count2 = 23
local stack2 = 3
local count3 = 32
local stack3 = 13
local spd = 4
local perAccel = 3.75

spellData.configure = {
	name = "Replicated Butterfly -NaN Reflowering-",
	timer = 150*60,
	playerCandidateRadius = 100,
	isSpell = true,
	caster = "LAMBDA"
}

spellData.onStart = function()
	boss:setMaxHP(75)
	spell:startTimer()
	spell:startCounting()
end

local waves = {}
local cooldowns = {120, 90, 140}

local function wave1bullet()
	return spawnBullet(
		sequence(
			var("type", mod(get("i"), 2) ),
			var("jndex", get("j")),
			var("kndex", get("k")),
			var("speed", spd),
			setGhost(100),
			setColor(165),
			jsif( equal( get("type"), 0),
				function()
					return changeColor(40)
				end
			),
			setBrightness(25),

			setCostume("BulletButterfly"),
			addCircleHitbox("bulletHB", 6),
			addHitboxTag("bulletHB", "ENEMY_BULLET"),
			addHitboxTag("bulletHB", "CLEARABLE"),

			parallel(
				sequence(
					warp(boss),
					look(0),
					turn( mul(get("type"), 360/2/count1) ),
					turn( mul( 360/count1, get("jndex") ) ),
					forever("sequence",
						forward( get("speed") ),
						wait()
					)
				),
				tween("size", 60, 27.5,
					"brightness", 100, 20,
					"ghost", 100, 0,
					45, easing.quadIn),		
				sequence(
					wait(120),
					jsif( equal( get("jndex"), get("kndex"), 0),
						function()
							return playSound("jingle")
						end
					),
					tween("speed", add(spd, mul(get("kndex"), perAccel) ),
						"brightness", sub(get("brightness"), mul(get("kndex"), 2.5)),
						"desaturation", mul(get("kndex"), 10), 90)
				),
				sequence(
					wait(270),
					disableHitbox("bulletHB"),
					tween("desaturation", 100, "ghost", 100, 90),
					destroy()
				)
			)
		)
	)
end
waves[1] = function()
	return jsfor("i", 1, 6, function()
		return sequence(
			playSound("shot"),
			jsfor("j", 0, count1-1, function()
				return jsfor("k", 0, stack1-1, function()
					return wave1bullet()
				end)
			end),
			wait( 12 )
		)
	end)
end

local function wave2bullet()
	return spawnBullet(
		sequence(
			var("index", mul(mod(get("i"), 2), 2)  ),
			change("index", -1),
			var("tndex", get("t")),
			var("jndex", get("j")),
			var("kndex", get("k")),
			var("speed", spd),
			change("speed", mul(get("t"), 1.5)),
			setGhost(100),
			setColor(0),

			setCostume("BulletButterfly"),
			addCircleHitbox("bulletHB", 6),
			addHitboxTag("bulletHB", "ENEMY_BULLET"),
			addHitboxTag("bulletHB", "CLEARABLE"),

			parallel(
				sequence(
					warp(boss),
					look( get("offset") ),
					turn( mul( 360/count2, get("jndex") ) ),
					forever("sequence",
						forward( get("speed") ),
						wait()
					)
				),
				tween("size", 60, 27.5,
					"brightness", 100, 20,
					"ghost", 100, 0,
					30, easing.quadIn),		
				sequence(
					turn( mul(45, get("index") ) , 30),
					turn( mul(22, get("index") ) ),
					wait(30),
					jsif( equal( get("jndex"), get("kndex"), 0),
						function()
							return playSound("jingle")
						end
					),
					tween("speed", add(spd, mul(get("kndex"), perAccel) ),
						"brightness", sub(get("brightness"), mul(get("kndex"), 2.5)),
						"desaturation", mul(get("kndex"), 10), 90)
				),
				sequence(
					wait(270),
					disableHitbox("bulletHB"),
					tween("desaturation", 100, "ghost", 100, 90),
					destroy()
				)
			)
		)
	)
end
waves[2] = function()
	return jsfor("i", 1, 6, function()
		return sequence(
			set("offset", random(1, 360)),
			playSound("shot"),
			jsfor("t", 0, 1, function()
				return jsfor("j", 0, count2-1, function()
					return jsfor("k", 0, stack2-1, function()
						return wave2bullet()
					end)
				end)
			end),
			wait( 30 )
		)
	end)
end

local function wave3bullet()
	return spawnBullet(
		sequence(
			var("jndex", get("j")),
			var("kndex", get("k")),
			var("speed", spd+2),
			setGhost(100),
			setColor(0),

			setCostume("BulletDonut"),
			addCircleHitbox("bulletHB", 25),
			addHitboxTag("bulletHB", "ENEMY_BULLET"),
			addHitboxTag("bulletHB", "CLEARABLE"),

			parallel(
				sequence(
					warp(boss),
					look( get("offset") ),
					turn( mul( 360/count3, get("jndex") ) ),
					forever("sequence",
						forward( get("speed") ),
						wait()
					)
				),
				tween("size", 200, 65,
					"brightness", 100, 20,
					"ghost", 100, 0,
					30, easing.quadIn),		
				sequence(
					wait(80),
					jsif( equal( get("jndex"), get("kndex"), 0),
						function()
							return playSound("jingle")
						end
					),
					tween("speed", add(spd+2, mul(get("kndex"), perAccel) ),
						"desaturation", mul(get("kndex"), 10), 40)
				),
				sequence(
					wait(210),
					disableHitbox("bulletHB"),
					tween("brightness", 100, "ghost", 100, 120),
					destroy()
				)
			)
		)
	)
end
local vert = 7
waves[3] = function()
	return sequence(
		set("offset", random(1, 360)),
		playSound("shot"),
		jsfor("j", 0, count3-1, function()
			return jsfor("k", 0-vert, stack3-1-vert, function()
				return wave3bullet()
			end)
		end)
	)
end

spellData.buildAction = function()
	return sequence(
		sound("jingle", "[TH] Jingle"),
		setSoundVolume("jingle", 5.25),

		sound("shot", "[TH] Shot"),
		setSoundVolume("shot", -5.5),
		
		var("offset", 0),
		forever("sequence",
			jsfor("wave", 1, 3, function()
				return sequence(
					callAt(waves, get("wave")),
					wait( at(cooldowns, get("wave")) )
				)
			end)
		)
	)
end

return spellData