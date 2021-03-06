/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package edu.harvard.hms.dbmi.bd2k.irct.model.resource.implementation;

import edu.harvard.hms.dbmi.bd2k.irct.model.resource.ResourceState;
import edu.harvard.hms.dbmi.bd2k.irct.model.result.ResultDataType;

/**
 * Provides an implementation that describes the API for any resource that has
 * visualization that can be created
 * 
 * @author Jeremy R. Easton-Marks
 *
 */
public interface VisualizationResourceImplementationInterface extends
		ResourceImplementationInterface {

	/**
	 * Returns the state of the resource
	 * 
	 * @return Resource State
	 */
	ResourceState getState();
	
	/**
	 * Returns the result data type
	 * 
	 * @return Result data type
	 */
	ResultDataType getVisualizationDataType();
}
