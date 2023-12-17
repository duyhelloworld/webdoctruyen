package ptit.edu.vn.entity;

public enum Role {
    ADMIN,
    USER;

    public static boolean isValidRole(String role) {
        for (Role r : Role.values()) {
            if (r.toString().equals(role))
                return true;
        }
        return false;
    }

    public static Role getRole(String role) {
        switch (role) {
            case "ADMIN":
                return ADMIN;  
            case "USER":
                return USER;      
            default:
                return null;
        }
    }
}
