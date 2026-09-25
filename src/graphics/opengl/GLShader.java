package graphics.opengl;

import org.lwjgl.opengl.GL20;
import main.Debug;

public class GLShader {
	private final int programId;

	public GLShader(String vertexSource, String fragmentSource) {
		int vertexShader = compile(
			GL20.GL_VERTEX_SHADER,
			vertexSource
		);

		int fragmentShader = compile(
			GL20.GL_FRAGMENT_SHADER,
			fragmentSource
		);

		programId = GL20.glCreateProgram();

		GL20.glAttachShader(programId, vertexShader);
		GL20.glAttachShader(programId, fragmentShader);
		GL20.glLinkProgram(programId);

		if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == 0) {
			String log = GL20.glGetProgramInfoLog(programId);
			throw Debug.terminate(this, "Failed to link shader program:\n" + log);
		}

		GL20.glDeleteShader(vertexShader);
		GL20.glDeleteShader(fragmentShader);
	}

	private int compile(int type, String source) {
		int shader = GL20.glCreateShader(type);

		GL20.glShaderSource(shader, source);
		GL20.glCompileShader(shader);

		if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == 0) {
			String log = GL20.glGetShaderInfoLog(shader);
			throw Debug.terminate(this, "Failed to compile shader:\n" + log);
		}

		return shader;
	}
	
	public void setInt(String name, int value) {
		int location = GL20.glGetUniformLocation(programId, name);

		if (location == -1) {
			return;
		}

		GL20.glUniform1i(location, value);
	}

	public void use() {
		GL20.glUseProgram(programId);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public void setFloat(String name, float value) {
		int location = GL20.glGetUniformLocation(programId, name);

		if (location == -1) {
			return;
		}

		GL20.glUniform1f(location, value);
	}

	public int getProgramId() {
		return programId;
	}

	public void destroy() {
		GL20.glDeleteProgram(programId);
	}
}