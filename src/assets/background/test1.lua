return {
	border = {
		x = 200,
		z = 200
	},

	camera = {
		position = { x = 0, y = 40, z = -250 },
		orientation = { pitch = 10, yaw = 0, roll = 0 }
	},

	world = {
		ground = {
			type = "plane",
			position = { x = 0, y = 0, z = 0 },
			orientation = { pitch = 90, yaw = 0, roll = 0 },
			costume = "Test2Ground"
		},
		tree1 = {
			type = "object",
			position = { x = 50, y = 30, z = 0 },
			orientation = { pitch = 0, yaw = 0, roll = 0 },
			costume = "Test2Tree"
		},
		tree2 = {
			type = "object",
			position = { x = -50, y = 30, z = 0 },
			orientation = { pitch = 0, yaw = 0, roll = 0 },
			costume = "Test2Tree"
		},
		tree3 = {
			type = "object",
			position = { x = 25, y = 30, z = 100 },
			orientation = { pitch = 0, yaw = 0, roll = 0 },
			costume = "Test2Tree"
		}
	},

	sky = {},

	events = {
		init = function(scene)
			return bestow("camera", function()
				return forever("sequence",
					change("z", 0.083333),
					wait(1)
				)
			end)
		end
	}
}
