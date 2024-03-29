package com.tamaproject.util;

/**
 * Constants used for multiplayer
 * 
 * @author Jonathan
 * 
 */
public interface TamaBattleConstants
{
    static final int SERVER_PORT = 4444;

    static final short FLAG_MESSAGE_SERVER_ADD_SPRITE = 1;
    static final short FLAG_MESSAGE_SERVER_MOVE_SPRITE = 2;
    static final short FLAG_MESSAGE_SERVER_ID_PLAYER = 3;
    static final short FLAG_MESSAGE_CLIENT_REQUEST_ID = 4, FLAG_MESSAGE_CLIENT_MOVE_SPRITE = 6,
	    FLAG_MESSAGE_CLIENT_ADD_SPRITE = 7, FLAG_MESSAGE_SERVER_FIRE_BULLET = 8,
	    FLAG_MESSAGE_CLIENT_FIRE_BULLET = 9, FLAG_MESSAGE_SERVER_REMOVE_SPRITE = 10,
	    FLAG_MESSAGE_SERVER_MODIFY_PLAYER = 11, FLAG_MESSAGE_CLIENT_SEND_PLAYER = 12,
	    FLAG_MESSAGE_SERVER_SEND_PLAYER = 13, FLAG_MESSAGE_SERVER_START_GAME = 14,
	    FLAG_MESSAGE_SERVER_RECEIVED_DAMAGE = 15, FLAG_MESSAGE_CLIENT_VOTE_DEATHMATCH = 16,
	    FLAG_MESSAGE_SERVER_DEATHMATCH = 16;

    static final int DIALOG_CHOOSE_SERVER_OR_CLIENT_ID = 0;
    static final int DIALOG_ENTER_SERVER_IP_ID = DIALOG_CHOOSE_SERVER_OR_CLIENT_ID + 1;
    static final int DIALOG_SHOW_SERVER_IP_ID = DIALOG_ENTER_SERVER_IP_ID + 1;
    static final int BAR_LENGTH = 100, BAR_HEIGHT = 15;

}
