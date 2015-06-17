grammar FieldPath;

options {
  language=Java;
}

@header {
/**
 * Copyright (c) 2014 MapR, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jackhammer;

import org.jackhammer.FieldPath;
import org.jackhammer.FieldSegment;
import org.jackhammer.FieldSegment.IndexSegment;
import org.jackhammer.FieldSegment.NameSegment;
}

/**
 * Parser rules
 */
parse returns [FieldPath e]
  : fieldSegment {$e = new FieldPath($fieldSegment.seg); }
  ;

fieldSegment returns [NameSegment seg]
  : s1 = nameSegment {$seg = $s1.seg;}
  ;

nameSegment returns [NameSegment seg]
  : QuotedIdentifier ((Period s1=fieldSegment) | s2=indexSegment)? {
      $seg = new NameSegment(
        $QuotedIdentifier.text.substring(1, $QuotedIdentifier.text.length()-1).replaceAll("\\\\(.)", "$1"),
        ($s1.start == null ? ($s2.start == null ? null : $s2.seg) : $s1.seg)
      , true);
    }
  | Identifier ((Period s1=fieldSegment) | s2=indexSegment)? {
      $seg = new NameSegment($Identifier.text,
        ($s1.start == null ? ($s2.start == null ? null : $s2.seg) : $s1.seg)
      , false);
    }
  | Integer ((Period s1=fieldSegment) | s2=indexSegment)? {
      $seg = new NameSegment($Integer.text,
        ($s1.start == null ? ($s2.start == null ? null : $s2.seg) : $s1.seg)
      , false);
    }
  ;

indexSegment returns [FieldSegment seg]
  : OBracket Integer? CBracket ((Period s1=fieldSegment) | s2=indexSegment)? {
      $seg = new IndexSegment($Integer.text,
        ($s1.start == null ? ($s2.start == null ? null : $s2.seg) : $s1.seg)
      );
    }
  ;


/**
 * Lexer rules
 */
Period : '.';
OBracket : '[';
CBracket : ']';
SingleQuote: '\'';

Integer
  :  Digit+
  ;

Identifier
  : ('a'..'z' | 'A'..'Z' | '_' | '$' | Digit)+
  ;

Space
  :  (' ' | '\t' | '\r' | '\n' | '\u000C') {skip();}
  ;

QuotedIdentifier
  :  '`'  (~('`' | '\\')  | '\\' ('\\' | '`'))+ '`'
  ;

fragment Digit
  :  '0'..'9'
  ;

ErrorChar
 : .
 ;
