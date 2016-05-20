/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package edu.harvard.hms.dbmi.bd2k.irct.model.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

import edu.harvard.hms.dbmi.bd2k.irct.model.resource.Resource;

/**
 * The query class represents any query against any individual or group of
 * resources. A Query can have many subQueries, and clauses (Joins, Selects,
 * Wheres).
 * 
 * @author Jeremy R. Easton-Marks
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Query implements Serializable {
	private static final long serialVersionUID = -407606258205399129L;

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	
	@ElementCollection
	@CollectionTable(name="query_subquery")
	@MapKeyColumn(name="query_id")
	private Map<Long, SubQuery> subQueries;
	
	@ElementCollection
	@CollectionTable(name="query_clause")
	@MapKeyColumn(name="query_id")
	private Map<Long, ClauseAbstract> clauses;

	@OneToMany
	private List<Resource> resources;

	/**
	 * Creates an empty query
	 * 
	 */
	public Query() {
		this.setSubQueries(new LinkedHashMap<Long, SubQuery>());
		this.setClauses(new LinkedHashMap<Long, ClauseAbstract>());
		this.setResources(new ArrayList<Resource>());
	}

	/**
	 * Returns a JSONObject representation of the object. This returns only the
	 * attributes associated with this object and not their representation.
	 * 
	 * This is equivalent of toJson(1);
	 * 
	 * @return JSON Representation
	 */
	public JsonObject toJson() {
		return toJson(1);
	}

	/**
	 * Returns a JSONObject representation of the object. This returns only the
	 * attributes associated with this object and not their representation.
	 * 
	 * 
	 * @param depth
	 *            Depth to travel
	 * @return JSON Representation
	 */
	public JsonObject toJson(int depth) {
		depth--;
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		// TODO: FILL IN
		return jsonBuilder.build();
	}

	// -------------------------------------------------------------------------
	// SETTERS AND GETTERS
	// -------------------------------------------------------------------------

	/**
	 * Returns the id of the query
	 * 
	 * @return ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id of the query
	 * 
	 * @param id
	 *            ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Returns the name of the query
	 * 
	 * @return Name of the Query
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the query
	 * 
	 * @param name Name of the query
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Adds a new subQuery to the query
	 * 
	 * @param id
	 *            SubQuery ID
	 * @param subQuery
	 *            SubQuery
	 */
	public final void addSubQuery(Long id, SubQuery subQuery) {
		this.subQueries.put(id, subQuery);
	}

	/**
	 * Removes a subQuery from the query
	 * 
	 * @param id
	 *            SubQuery ID
	 */
	public final void removeSubQuery(Long id) {
		this.subQueries.remove(id);
	}

	/**
	 * Returns a map of SubQueries where the key is the subQuery ID, and the
	 * value is the subQuery
	 * 
	 * @return SubQuery Map
	 */
	public final Map<Long, SubQuery> getSubQueries() {
		return subQueries;
	}

	/**
	 * Sets a map of SubQueries where the key is the subQuery ID, and the value
	 * is the subQuery
	 * 
	 * @param subQueries
	 *            SubQuery Map
	 */
	public final void setSubQueries(Map<Long, SubQuery> subQueries) {
		this.subQueries = subQueries;
	}

	/**
	 * Adds a new clause to the subQuery
	 * 
	 * @param id
	 *            Clause ID
	 * @param clause
	 *            Clause
	 */
	public void addClause(Long id, ClauseAbstract clause) {
		this.clauses.put(id, clause);
	}

	/**
	 * Removes a clause from the subQuery
	 * 
	 * @param id
	 *            Clause ID
	 */
	public void removeClause(Long id) {
		this.clauses.remove(id);
	}

	/**
	 * Returns a map of Clauses where the key is the clause id, and the Clause
	 * is the value
	 * 
	 * @return Clause Map
	 */
	public Map<Long, ClauseAbstract> getClauses() {
		return clauses;
	}

	/**
	 * Sets a map of Clauses where the key is the clause id, and the Clause is
	 * the value
	 * 
	 * @param clauses Map of Clauses
	 */
	public void setClauses(Map<Long, ClauseAbstract> clauses) {
		this.clauses = clauses;
	}

	/**
	 * Adds a resource to the query
	 * 
	 * @param resource
	 *            Resource
	 */
	public void addResource(Resource resource) {
		this.resources.add(resource);
	}

	/**
	 * Removes a resource from the query
	 * 
	 * @param resource
	 *            Resource
	 */
	public void removeResource(Resource resource) {
		this.resources.remove(resource);
	}

	/**
	 * Returns a list of resources that are associated with the query
	 * 
	 * @return Resources
	 */
	public List<Resource> getResources() {
		return resources;
	}

	/**
	 * Sets a list of resources that are associated with the query
	 * 
	 * @param resources
	 *            Resources
	 */
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
}
