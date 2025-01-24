package it.unibo.templetower.util;

public class SystemInfo {
    private final String lineSeparator;
    private final String fileSeparator;
    private final String osName;
    private final String userName;
    private final String javaVersion;
    private final String userDir;
    private final String userHome;

    public SystemInfo() {
        this.lineSeparator = System.getProperty("line.separator");
        this.fileSeparator = System.getProperty("file.separator");
        this.osName = System.getProperty("os.name");
        this.userName = System.getProperty("user.name");
        this.javaVersion = System.getProperty("java.version");
        this.userDir = System.getProperty("user.dir");
        this.userHome = System.getProperty("user.home");
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    public String getFileSeparator() {
        return fileSeparator;
    }

    public String getOsName() {
        return osName;
    }

    public String getUserName() {
        return userName;
    }
    public String getJavaVersion() {
        return javaVersion;
    }

    public String getUserDir() {
        return userDir;
    }

    public String getUserHome() {
        return userHome;
    }
}