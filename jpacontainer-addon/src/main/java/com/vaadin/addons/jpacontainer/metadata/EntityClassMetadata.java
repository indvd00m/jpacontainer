/*
 * JPAContainer
 * Copyright (C) 2009 Oy IT Mill Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.vaadin.addons.jpacontainer.metadata;

import javax.persistence.Entity;

/**
 * An extended version of {@link ClassMetadata} that provides additional information about
 * classes annotated with the {@link Entity} annotation.
 *
 * @author Petter Holmström (IT Mill)
 * @since 1.0
 */
public class EntityClassMetadata<T> extends ClassMetadata<T> {

    private final String entityName;
    private String versionProperty;
    private String identifierProperty;

    /**
     * Creates a new instance of <code>EntityClassMetadata</code>.
     *
     * @param mappedClass the entity class (must not be null).
     * @param entityName the entity name (must not be null).
     */
    EntityClassMetadata(Class<T> mappedClass, String entityName) {
        super(mappedClass);
        assert entityName != null : "entityName must not be null";
        this.entityName = entityName;
    }

    /**
     * Sets the name of the property that contains the version, if any.
     * @param propertyName the property name, may be null.
     */
    void setVersionPropertyName(String propertyName) {
        if (propertyName != null) {
            PropertyMetadata pm = getProperty(propertyName);
            if (pm == null || !(pm instanceof PersistentPropertyMetadata)) {
                throw new IllegalArgumentException("Invalid property");
            }
        }
        this.versionProperty = propertyName;
    }

    /**
     * Sets the name of the property that contains the identifier, if any.
     *
     * @param propertyName the property name, may be null.
     * @throws IllegalArgumentException if <code>propertyName</code> is invalid (i.e. the property does not exist or is transient).
     */
    void setIdentifierPropertyName(String propertyName) throws
            IllegalArgumentException {
        if (propertyName != null) {
            PropertyMetadata pm = getProperty(propertyName);
            if (pm == null || !(pm instanceof PersistentPropertyMetadata)) {
                throw new IllegalArgumentException("Invalid property");
            }
        }
        this.identifierProperty = propertyName;
    }

    /**
     * The name of the entity. If no explicit entity name has been given,
     * this is the simple class name.
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * If the entity has a version property or  not.
     *
     * @see #getVersionProperty()
     */
    public boolean hasVersionProperty() {
        return versionProperty != null;
    }

    /**
     * Gets the version property, if it exists.
     *
     * @see #hasVersionProperty() 
     * @return the version property metadata, or null if not available.
     */
    public PersistentPropertyMetadata getVersionProperty() {
        return versionProperty == null ? null : (PersistentPropertyMetadata) getProperty(
                versionProperty);
    }

    /**
     * If the entity has an identifier property or not.
     * 
     * @see #getIdentifierProperty()
     * @see #hasEmbeddedIdentifier()
     */
    public boolean hasIdentifierProperty() {
        return identifierProperty != null;
    }

    /**
     * Gets the identifier property, if it exists. If {@link #hasEmbeddedIdentifier() } returns true,
     * this property is the embedded identifier.
     *
     * @see #hasIdentifierProperty()
     * @see #hasEmbeddedIdentifier() 
     * @return the identifier property metadata, or null if not available.
     */
    public PersistentPropertyMetadata getIdentifierProperty() {
        return identifierProperty == null ? null : (PersistentPropertyMetadata) getProperty(
                identifierProperty);
    }

    /**
     * If the entity has an embedded identifier. This property cannot be
     * true unless {@link #hasIdentifierProperty() } also returns true.
     */
    public boolean hasEmbeddedIdentifier() {
        return hasIdentifierProperty() && getIdentifierProperty().
                getPropertyKind() == PersistentPropertyMetadata.PropertyKind.EMBEDDED;
    }
}