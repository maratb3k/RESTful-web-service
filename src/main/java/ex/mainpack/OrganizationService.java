package ex.mainpack;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Or;

import javax.management.OperationsException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class OrganizationService {
    private ConcurrentMap<Integer, Organization> organizations;
    private AtomicInteger key;

    public OrganizationService() {
        this.organizations = new ConcurrentHashMap<>();
        this.key = new AtomicInteger();

        this.addOrganization(new Organization(1, "ABC", "ABC123", LocalDate.of(2020, 10, 8)));
        this.addOrganization(new Organization(2, "ABD", "ABD443", LocalDate.of(2003, 05, 5)));
        this.addOrganization(new Organization(2, "KEY", "KEY888", LocalDate.of(2010, 11, 20)));
    }

    private boolean addOrganization(Organization organization) {
        Integer id = key.incrementAndGet();
        organization.setId(id);
        this.organizations.put(id, organization);
        return true;
    }

    public String putOrganization(Organization organization) {
        Integer org_id = key.incrementAndGet();
        organizations.put(org_id, organization);
        return toJson(organization);
    }

    public String deleteOrganization(Integer id) {
        String jsonResp = toJson(this.organizations.get(id));
        this.organizations.remove(id);
        return jsonResp;
    }

    public String findAllOrganizations() {
        List<Organization> list = new ArrayList<>(this.organizations.values());
        return toJson(list);
    }

    private String toJson(Object list) {
        if(list == null) return null;
        Gson gson = new Gson();
        String json = null;
        try {
            json = gson.toJson(list);
        }
        catch (Exception e) {}
        return json;
    }
}
