package attribute;

public class Attribute {
    private String attributeName;
    private Class<?> attributeClass;

    public Attribute(String attributeName, Class<?> attributeClass) {
        this.attributeName = attributeName;
        this.attributeClass = attributeClass;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Class<?> getAttributeClass() {
        return attributeClass;
    }

    public void setAttributeClass(Class<?> attributeClass) {
        this.attributeClass = attributeClass;
    }

    public boolean checkValue(Object value) {
        return attributeClass.isInstance(value);
    }
}
