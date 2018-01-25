package com.dber.upload.server.valid;

import com.dber.upload.service.IDfileErrorService;
import com.dber.upload.service.IDfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;

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
public class IDGeneratorService implements IIDGenerator {
    private AtomicLong current;

    @Autowired
    private IDfileService service;

    @Autowired
    private IDfileErrorService errorService;

    @Override
    public long next() {
        return current.getAndIncrement();
    }

    @Override
    public long current() {
        return current.get();
    }

    @PostConstruct
    private void init() {
        Long serviceId = service.getMaxId();
        serviceId = serviceId == null ? 10000 : serviceId;

        Long errorServiceId = errorService.getMaxId();
        errorServiceId = errorServiceId == null ? serviceId : errorServiceId;

        this.current = new AtomicLong(serviceId > errorServiceId ? serviceId : errorServiceId);
    }
}
