/*******************************************************************************
 * Copyright (c) 2014 OSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    James Williams - Implementation.
 *******************************************************************************/
package org.eclipse.scava.platform.app.example;

import java.util.List;

import com.mongodb.Mongo;

public class ResetApp {

	public static void main(String[] args) throws Exception {

		Mongo mongo = new Mongo();
		List<String> x = mongo.getDatabaseNames();
		System.out.println(x.size()-1 + " databases will be dropped");

		for (String dbName : x){

			if(!dbName.equals("admin")) {
				mongo.dropDatabase(dbName);
				System.out.println("\t *" + dbName + " has been dropped");

			}
		}
			System.out.println("Database reset complete");
	}
}
