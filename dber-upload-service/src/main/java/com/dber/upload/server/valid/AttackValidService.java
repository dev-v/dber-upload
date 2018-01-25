package com.dber.upload.server.valid;

import com.dber.base.enums.ImgErrorStatus;
import com.dber.base.enums.ImgErrorType;
import com.dber.base.mybatis.plugin.pagination.page.Page;
import com.dber.upload.api.entity.DfileError;
import com.dber.upload.service.IDfileErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/25
 */
@Service
public class AttackValidService implements IAttackValid {

    @Autowired
    private IDfileErrorService errorService;

    /**
     * 上传疑似攻击行为集合 值为：bsId-type
     * 拒绝该类id申请上传token
     */
    Set<String> attackMap = new HashSet<>();

    @PostConstruct
    private void init() {
        Page<DfileError> page = new Page<>(1, 30000);
        DfileError error = new DfileError();
        error.setErrorType(ImgErrorType.ATTACK.getValue());
        error.setStatus(ImgErrorStatus.VALID.getValue());
        page.setCondition(error);
        errorService.query(page);

        for (DfileError file : page.getDatas()) {
            attackMap.add(getKey(file.getType(), file.getBsId()));
        }
    }

    @Override
    public void isAttack(int type, long bsId) {
        if (attackMap.contains(getKey(type, bsId))) {
            throw new IllegalArgumentException("invalid upload access error！");
        }
    }

    @Override
    public void remove(int type, long bsId) {
        attackMap.remove(getKey(type, bsId));
    }

    @Override
    public void add(int type, long bsId) {
        attackMap.add(getKey(type, bsId));
    }

    private String getKey(int type, long bsId) {
        return type + "-" + bsId; //不要试图改为''
    }
}
