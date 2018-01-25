package com.dber.upload.server.valid;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/25
 */
public interface IAttackValid {
    /**
     * 在攻击列表中抛出异常
     *
     * @param type
     * @param bsId
     * @throws IllegalArgumentException
     */
    void isAttack(int type, long bsId) throws IllegalArgumentException;

    /**
     * 移除攻击列表
     *
     * @param type
     * @param bsId
     */
    void remove(int type, long bsId);

    /**
     * 添加到攻击列表
     *
     * @param type
     * @param bsId
     */
    void add(int type, long bsId);
}
