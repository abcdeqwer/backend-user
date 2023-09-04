package io.ram.enums;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 帐变类型
 */
@Getter
@AllArgsConstructor
public enum TransferType {
    DEPOSIT("deposit","存款"),
    WITHDRAWAL("withdrawal","取款"),
    WITHDRAWAL_RETURN("withdrawal_return","取款回退"),
    LOCAL_TO_LOBBY("localToLobby","从本地转至游戏厅"),
    LOBBY_TO_LOCAL("lobbyToLocal","从游戏厅至本地")
    ;

    @EnumValue
    private String code;
    private String desc;
}
