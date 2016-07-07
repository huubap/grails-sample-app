package com.yourapp

class User {

	transient springSecurityService

	String username
	String altUsername
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static transients = ['springSecurityService']

	static belongsTo = [ profile: Profile, tenant: Tenant ]

	static constraints = {
		username blank: false, unique: 'tenant'
		altUsername blank: false, unique: 'tenant'
		password blank: false
		profile nullable: true
	}

	static mapping = {
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
}
