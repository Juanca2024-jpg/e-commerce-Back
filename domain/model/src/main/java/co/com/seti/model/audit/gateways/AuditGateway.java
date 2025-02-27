package co.com.seti.model.audit.gateways;

import co.com.seti.model.audit.Audit;

import java.util.List;

public interface AuditGateway {
    
    Audit save(Audit audit);

    List<Audit> findAll();
    
}
