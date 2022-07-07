package pr4;

public class ProductFilter {
    private Double priceFrom;
    private Double priceTo;
    private String nameStartWith;
    private String nameEndWith;

    public Double getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Double priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Double getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Double priceTo) {
        this.priceTo = priceTo;
    }

    public String getNameStartWith() {
        return nameStartWith;
    }

    public void setNameStartWith(String nameStartWith) {
        this.nameStartWith = nameStartWith;
    }

    public String getNameEndWith() {
        return nameEndWith;
    }

    public void setNameEndWith(String nameEndWith) {
        this.nameEndWith = nameEndWith;
    }
}
