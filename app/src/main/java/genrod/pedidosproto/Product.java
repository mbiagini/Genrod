package genrod.pedidosproto;

/**
 * Created by Martin on 24/08/2017.
 */

public class Product {

    private final String code;
    private final String alternative;
    private final String description;
    private String amount;
    private final String location;
    private final String seller;
    private final String client;
    private final String clientName;
    private final String orderType;
    private final String auditLogUser;
    private final String regId;
    private String motive;
    private String prepared;

    public Product(String code, String alternative, String description, String amount, String location,
                   String seller, String client, String clientName, String orderType, String regId,
                   String auditLogUser, String motive, String prepared) {
        this.code = code;
        this.alternative = alternative;
        this.description = description;
        this.amount = amount;
        this.location = location;
        this.seller = seller;
        this.client = client;
        this.clientName = clientName;
        this.orderType = orderType;
        this.auditLogUser = auditLogUser;
        this.regId = regId;
        this.motive = motive;
        this.prepared = prepared;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public void setPrepared(String prepared) {
        this.prepared = prepared;
    }

    public String getAmount() {
        return amount;
    }

    public String getCode() {
        return code;
    }

    public String getAlternative() {
        return alternative;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getClient() {
        return client;
    }

    public String getSeller() {
        return seller;
    }

    public String getClientName() {
        return clientName;
    }

    public String getOrderType() {
        return orderType;
    }

    public String getAuditLogUser() {
        return auditLogUser;
    }

    public String getRegId() {
        return regId;
    }

    public String getMotive() {
        return motive;
    }

    public String getPrepared() {
        return prepared;
    }
}
