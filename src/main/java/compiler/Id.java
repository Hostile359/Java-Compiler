package compiler;

public class Id {
    private String name;
    private String type;
    private int level;
    private boolean isInit;

    public Id(String name, String type, int level) {
        this.name = name;
        this.type = type;
        this.level = level;
    }

    public Id(String name, String type, int level, boolean isInit) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.isInit = isInit;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public boolean isInit() {
        return isInit;
    }

    public int getLevel() {
        return level;
    }

    public void print(){
        if(name != null || type != null) {
            System.out.println("Id: " + name + " : " + type + ", level-" + level);
        }
    }
}
