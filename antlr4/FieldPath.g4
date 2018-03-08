grammar FieldPath;

options {
  language=Java;
}

@header {
/**
 * Copyright (c) 2015 MapR, Inc.
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
package org.ojai.antlr4;

import static org.ojai.FieldSegment.NameSegment.unEscape;
import static org.ojai.FieldSegment.NameSegment.unQuote;

import org.ojai.FieldPath;
import org.ojai.FieldSegment;
import org.ojai.FieldSegment.IndexSegment;
import org.ojai.FieldSegment.NameSegment;
}

/**
 * Parser rules
 */
parse returns [FieldPath fp]
  : fieldSegment {$fp = new FieldPath($fieldSegment.seg); }
  ;

fieldSegment returns [NameSegment seg]
  : s1 = nameSegment {$seg = $s1.seg;}
  ;

nameSegment returns [NameSegment seg]
  : QuotedIdentifier ((Period s1=fieldSegment) | s2=indexSegment)? {
      $seg = new NameSegment(unQuote($QuotedIdentifier.text),
        ($s1.start == null ? ($s2.start == null ? null : $s2.seg) : $s1.seg)
      , true);
    }
  | Identifier ((Period s1=fieldSegment) | s2=indexSegment)? {
      $seg = new NameSegment(unEscape($Identifier.text),
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
DoubleQuote: '"';
BackTick: '`';

Space
  :  ' ' {skip();}
  ;

Integer
  :  Digit+
  ;

Identifier
  :  IdentifierChar+
  ;

QuotedIdentifier
  :  DoubleQuote QuotedIdentifierChar* DoubleQuote
  |  BackTick QuotedIdentifierChar* BackTick
  ;

fragment Digit
  :  [0-9]
  ;

fragment HexDigit
  :  [0-9a-fA-F]
  ;

fragment IdentifierChar
  :  ~[`"\\\b\f\n\r\t.[\]]
  |  EscapeChar
  ;

fragment QuotedIdentifierChar
  :  ~[`"\\\b\f\n\r\t]
  |  EscapeChar
  ;

fragment EscapeChar
  :  SpecialChar
  |  ControlChar
  |  UnicodeChar
  ;

fragment SpecialChar
  :  '\\' [`"\\.[\]/] /* '/' can be appear without escaping */
  ;

fragment ControlChar
  :  '\\' [bfnrt]
  ;

fragment UnicodeChar
  :  '\\u' HexDigit HexDigit HexDigit HexDigit
  ;

ErrorChar
  :  .
  ;
