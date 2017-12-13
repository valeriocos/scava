@license{
Copyright (c) 2014 CROSSMETER Partners.
All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v1.0
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/epl-v10.html
}
module ck::NOA

import Set;

@doc{
	Number of attributes per class
}
public map[loc, int] NOA(map[loc \type, set[loc] attributes] typeAttributes, set[loc] allTypes) {
	return ( t : size(typeAttributes[t]?{}) | t <- allTypes );
}

/*
int NOA(M3 model) = (0 | it + 1 | entity <- model@declarations<0>, isField(entity));
                                  
map[loc class, int methodCount] NOAperClass(M3 model) {
  map[loc, int] result = ();
  for (<class, field> <- model@containment, canContainMethods(class), isField(method)) {
    result[class] ? 0 += 1;
  }
  return result;
}

// Generic method for all number of methods related metrics
public map[loc, int] NOA(M3 m, set[Modifier] modifiers = {}) {
  classFields = declaredFields(m, checkModifiers = modifiers);
  classFieldsMap = toMap(classFields);
  return (class : size(classFieldsMap[class]) | class <- classFieldsMap);
}
*/
