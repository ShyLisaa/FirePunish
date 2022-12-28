package dev.shylisaa.firepunish.api.database.type;

public enum DatabaseType {

    VARCHAR("varchar(255)"),
    INT("int"),
    LONG("long"),
    TEXT("text"),
    JSON("json");

    private final String display;

    DatabaseType(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
