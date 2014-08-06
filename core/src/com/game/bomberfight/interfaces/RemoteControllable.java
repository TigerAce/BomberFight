package com.game.bomberfight.interfaces;

import com.game.bomberfight.enums.Direction;

public interface RemoteControllable {
	public abstract void startMovePlayer(Direction direction);
	public abstract void stopMovePlayer();
}
