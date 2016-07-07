import com.yourapp.Profile
import com.yourapp.Role
import com.yourapp.Tenant
import com.yourapp.User
import com.yourapp.UserRole

class BootStrap {

    def init = { servletContext ->
        def adminRole = new Role(authority: "ROLE_ADMIN").save()
        def userRole = new Role(authority: "ROLE_USER").save()

        // Tenant A
        def tenantA = new Tenant(code: "a").save()

        def adminProfileOfTenantA = new Profile(tenant: tenantA).save()
        def userProfileOfTenantA = new Profile(tenant: tenantA).save()

        def userWithAdminRoleOfTenantA = new User(username: "admin@huubap.com", altUsername: "admin", password: "pass", tenant: tenantA, profile: adminProfileOfTenantA).save()
        def userWithUserRoleOfTenantA = new User(username: "user@huubap.com", altUsername: "user", password: "pass", tenant: tenantA, profile: userProfileOfTenantA).save()

        UserRole.create(userWithAdminRoleOfTenantA, adminRole, true)
        UserRole.create(userWithUserRoleOfTenantA, userRole, true)

        // Tenant B
        def tenantB = new Tenant(code: "b").save()

        def adminProfileOfTenantB = new Profile(tenant: tenantB).save()

        def userWithAdminRoleOfTenantB = new User(username: "admin@huubap.com", altUsername: "admin", password: "pass", tenant: tenantB, profile: adminProfileOfTenantB).save()

        UserRole.create(userWithAdminRoleOfTenantB, adminRole, true)
    }
    def destroy = {
    }
}
