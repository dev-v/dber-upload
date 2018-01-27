package com.dber.upload.web;

import com.dber.base.entity.Account;
import com.dber.base.result.Result;
import com.dber.plat.api.PlatLoginHelper;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/27
 */
public class UploadLoginHelper extends PlatLoginHelper {
    @Override
    public Result<Account> saveAccount(Account account) {
        return null;
    }

    @Override
    protected void addAccount(Account account) {
    }
}
