# Generated from antlr4/FieldPathPython.g4 by ANTLR 4.7.1
# encoding: utf-8
from __future__ import print_function
from antlr4 import *
from io import StringIO
import sys


"""
  Copyright (c) 2018 MapR, Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 """
from ojai.fields.impl.Segment import NameSegment, IndexSegment
from ojai.fields.FieldPath import FieldPath

def serializedATN():
    with StringIO() as buf:
        buf.write(u"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3")
        buf.write(u"\f\64\4\2\t\2\4\3\t\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\3")
        buf.write(u"\3\3\3\3\3\4\3\4\3\4\3\4\5\4\25\n\4\3\4\3\4\3\4\3\4\3")
        buf.write(u"\4\5\4\34\n\4\3\4\3\4\3\4\3\4\3\4\5\4#\n\4\3\4\5\4&\n")
        buf.write(u"\4\3\5\3\5\5\5*\n\5\3\5\3\5\3\5\3\5\5\5\60\n\5\3\5\3")
        buf.write(u"\5\3\5\2\2\6\2\4\6\b\2\2\2:\2\n\3\2\2\2\4\r\3\2\2\2\6")
        buf.write(u"%\3\2\2\2\b\'\3\2\2\2\n\13\5\4\3\2\13\f\b\2\1\2\f\3\3")
        buf.write(u"\2\2\2\r\16\5\6\4\2\16\17\b\3\1\2\17\5\3\2\2\2\20\24")
        buf.write(u"\7\13\2\2\21\22\7\3\2\2\22\25\5\4\3\2\23\25\5\b\5\2\24")
        buf.write(u"\21\3\2\2\2\24\23\3\2\2\2\24\25\3\2\2\2\25\26\3\2\2\2")
        buf.write(u"\26&\b\4\1\2\27\33\7\n\2\2\30\31\7\3\2\2\31\34\5\4\3")
        buf.write(u"\2\32\34\5\b\5\2\33\30\3\2\2\2\33\32\3\2\2\2\33\34\3")
        buf.write(u"\2\2\2\34\35\3\2\2\2\35&\b\4\1\2\36\"\7\t\2\2\37 \7\3")
        buf.write(u"\2\2 #\5\4\3\2!#\5\b\5\2\"\37\3\2\2\2\"!\3\2\2\2\"#\3")
        buf.write(u"\2\2\2#$\3\2\2\2$&\b\4\1\2%\20\3\2\2\2%\27\3\2\2\2%\36")
        buf.write(u"\3\2\2\2&\7\3\2\2\2\')\7\4\2\2(*\7\t\2\2)(\3\2\2\2)*")
        buf.write(u"\3\2\2\2*+\3\2\2\2+/\7\5\2\2,-\7\3\2\2-\60\5\4\3\2.\60")
        buf.write(u"\5\b\5\2/,\3\2\2\2/.\3\2\2\2/\60\3\2\2\2\60\61\3\2\2")
        buf.write(u"\2\61\62\b\5\1\2\62\t\3\2\2\2\b\24\33\"%)/")
        return buf.getvalue()


class FieldPathPythonParser ( Parser ):

    grammarFileName = "FieldPathPython.g4"

    atn = ATNDeserializer().deserialize(serializedATN())

    decisionsToDFA = [ DFA(ds, i) for i, ds in enumerate(atn.decisionToState) ]

    sharedContextCache = PredictionContextCache()

    literalNames = [ u"<INVALID>", u"'.'", u"'['", u"']'", u"'\"'", u"'`'", 
                     u"' '" ]

    symbolicNames = [ u"<INVALID>", u"Period", u"OBracket", u"CBracket", 
                      u"DoubleQuote", u"BackTick", u"Space", u"Integer", 
                      u"Identifier", u"QuotedIdentifier", u"ErrorChar" ]

    RULE_parse = 0
    RULE_field_segment = 1
    RULE_name_segment = 2
    RULE_index_segment = 3

    ruleNames =  [ u"parse", u"field_segment", u"name_segment", u"index_segment" ]

    EOF = Token.EOF
    Period=1
    OBracket=2
    CBracket=3
    DoubleQuote=4
    BackTick=5
    Space=6
    Integer=7
    Identifier=8
    QuotedIdentifier=9
    ErrorChar=10

    def __init__(self, input, output=sys.stdout):
        super(FieldPathPythonParser, self).__init__(input, output=output)
        self.checkVersion("4.7.1")
        self._interp = ParserATNSimulator(self, self.atn, self.decisionsToDFA, self.sharedContextCache)
        self._predicates = None



    class ParseContext(ParserRuleContext):

        def __init__(self, parser, parent=None, invokingState=-1):
            super(FieldPathPythonParser.ParseContext, self).__init__(parent, invokingState)
            self.parser = parser
            self.fp = None
            self._field_segment = None # Field_segmentContext

        def field_segment(self):
            return self.getTypedRuleContext(FieldPathPythonParser.Field_segmentContext,0)


        def getRuleIndex(self):
            return FieldPathPythonParser.RULE_parse

        def enterRule(self, listener):
            if hasattr(listener, "enterParse"):
                listener.enterParse(self)

        def exitRule(self, listener):
            if hasattr(listener, "exitParse"):
                listener.exitParse(self)




    def parse(self):

        localctx = FieldPathPythonParser.ParseContext(self, self._ctx, self.state)
        self.enterRule(localctx, 0, self.RULE_parse)
        try:
            self.enterOuterAlt(localctx, 1)
            self.state = 8
            localctx._field_segment = self.field_segment()
            localctx.fp =  FieldPath(localctx._field_segment.seg) 
        except RecognitionException as re:
            localctx.exception = re
            self._errHandler.reportError(self, re)
            self._errHandler.recover(self, re)
        finally:
            self.exitRule()
        return localctx

    class Field_segmentContext(ParserRuleContext):

        def __init__(self, parser, parent=None, invokingState=-1):
            super(FieldPathPythonParser.Field_segmentContext, self).__init__(parent, invokingState)
            self.parser = parser
            self.seg = None
            self.s1 = None # Name_segmentContext

        def name_segment(self):
            return self.getTypedRuleContext(FieldPathPythonParser.Name_segmentContext,0)


        def getRuleIndex(self):
            return FieldPathPythonParser.RULE_field_segment

        def enterRule(self, listener):
            if hasattr(listener, "enterField_segment"):
                listener.enterField_segment(self)

        def exitRule(self, listener):
            if hasattr(listener, "exitField_segment"):
                listener.exitField_segment(self)




    def field_segment(self):

        localctx = FieldPathPythonParser.Field_segmentContext(self, self._ctx, self.state)
        self.enterRule(localctx, 2, self.RULE_field_segment)
        try:
            self.enterOuterAlt(localctx, 1)
            self.state = 11
            localctx.s1 = self.name_segment()
            localctx.seg =  localctx.s1.seg
        except RecognitionException as re:
            localctx.exception = re
            self._errHandler.reportError(self, re)
            self._errHandler.recover(self, re)
        finally:
            self.exitRule()
        return localctx

    class Name_segmentContext(ParserRuleContext):

        def __init__(self, parser, parent=None, invokingState=-1):
            super(FieldPathPythonParser.Name_segmentContext, self).__init__(parent, invokingState)
            self.parser = parser
            self.seg = None
            self._QuotedIdentifier = None # Token
            self.s1 = None # Field_segmentContext
            self.s2 = None # Index_segmentContext
            self._Identifier = None # Token
            self._Integer = None # Token

        def QuotedIdentifier(self):
            return self.getToken(FieldPathPythonParser.QuotedIdentifier, 0)

        def index_segment(self):
            return self.getTypedRuleContext(FieldPathPythonParser.Index_segmentContext,0)


        def Period(self):
            return self.getToken(FieldPathPythonParser.Period, 0)

        def field_segment(self):
            return self.getTypedRuleContext(FieldPathPythonParser.Field_segmentContext,0)


        def Identifier(self):
            return self.getToken(FieldPathPythonParser.Identifier, 0)

        def Integer(self):
            return self.getToken(FieldPathPythonParser.Integer, 0)

        def getRuleIndex(self):
            return FieldPathPythonParser.RULE_name_segment

        def enterRule(self, listener):
            if hasattr(listener, "enterName_segment"):
                listener.enterName_segment(self)

        def exitRule(self, listener):
            if hasattr(listener, "exitName_segment"):
                listener.exitName_segment(self)




    def name_segment(self):

        localctx = FieldPathPythonParser.Name_segmentContext(self, self._ctx, self.state)
        self.enterRule(localctx, 4, self.RULE_name_segment)
        try:
            self.state = 35
            self._errHandler.sync(self)
            token = self._input.LA(1)
            if token in [FieldPathPythonParser.QuotedIdentifier]:
                self.enterOuterAlt(localctx, 1)
                self.state = 14
                localctx._QuotedIdentifier = self.match(FieldPathPythonParser.QuotedIdentifier)
                self.state = 18
                self._errHandler.sync(self)
                token = self._input.LA(1)
                if token in [FieldPathPythonParser.Period]:
                    self.state = 15
                    self.match(FieldPathPythonParser.Period)
                    self.state = 16
                    localctx.s1 = self.field_segment()
                    pass
                elif token in [FieldPathPythonParser.OBracket]:
                    self.state = 17
                    localctx.s2 = self.index_segment()
                    pass
                elif token in [FieldPathPythonParser.EOF]:
                    pass
                else:
                    pass

                localctx.seg = NameSegment(
                      name=NameSegment.un_quote((None if localctx._QuotedIdentifier is None else localctx._QuotedIdentifier.text)),
                      quoted=True,
                      child=(None if (None if localctx.s1 is None else localctx.s1.start) is None and (None if localctx.s2 is None else localctx.s2.start) is None else localctx.s2.seg if (None if localctx.s1 is None else localctx.s1.start) is None and (None if localctx.s2 is None else localctx.s2.start) is not None else localctx.s1.seg))

                pass
            elif token in [FieldPathPythonParser.Identifier]:
                self.enterOuterAlt(localctx, 2)
                self.state = 21
                localctx._Identifier = self.match(FieldPathPythonParser.Identifier)
                self.state = 25
                self._errHandler.sync(self)
                token = self._input.LA(1)
                if token in [FieldPathPythonParser.Period]:
                    self.state = 22
                    self.match(FieldPathPythonParser.Period)
                    self.state = 23
                    localctx.s1 = self.field_segment()
                    pass
                elif token in [FieldPathPythonParser.OBracket]:
                    self.state = 24
                    localctx.s2 = self.index_segment()
                    pass
                elif token in [FieldPathPythonParser.EOF]:
                    pass
                else:
                    pass

                localctx.seg = NameSegment(
                      name=NameSegment.un_escape((None if localctx._Identifier is None else localctx._Identifier.text)),
                      quoted=False,
                      child=(None if (None if localctx.s1 is None else localctx.s1.start) is None and (None if localctx.s2 is None else localctx.s2.start) is None else localctx.s2.seg if (None if localctx.s1 is None else localctx.s1.start) is None and (None if localctx.s2 is None else localctx.s2.start) is not None else localctx.s1.seg))

                pass
            elif token in [FieldPathPythonParser.Integer]:
                self.enterOuterAlt(localctx, 3)
                self.state = 28
                localctx._Integer = self.match(FieldPathPythonParser.Integer)
                self.state = 32
                self._errHandler.sync(self)
                token = self._input.LA(1)
                if token in [FieldPathPythonParser.Period]:
                    self.state = 29
                    self.match(FieldPathPythonParser.Period)
                    self.state = 30
                    localctx.s1 = self.field_segment()
                    pass
                elif token in [FieldPathPythonParser.OBracket]:
                    self.state = 31
                    localctx.s2 = self.index_segment()
                    pass
                elif token in [FieldPathPythonParser.EOF]:
                    pass
                else:
                    pass

                localctx.seg = NameSegment(
                      name=(None if localctx._Integer is None else localctx._Integer.text),
                      quoted=False,
                      child=(None if (None if localctx.s1 is None else localctx.s1.start) is None and (None if localctx.s2 is None else localctx.s2.start) is None else localctx.s2.seg if (None if localctx.s1 is None else localctx.s1.start) is None and (None if localctx.s2 is None else localctx.s2.start) is not None else localctx.s1.seg))

                pass
            else:
                raise NoViableAltException(self)

        except RecognitionException as re:
            localctx.exception = re
            self._errHandler.reportError(self, re)
            self._errHandler.recover(self, re)
        finally:
            self.exitRule()
        return localctx

    class Index_segmentContext(ParserRuleContext):

        def __init__(self, parser, parent=None, invokingState=-1):
            super(FieldPathPythonParser.Index_segmentContext, self).__init__(parent, invokingState)
            self.parser = parser
            self.seg = None
            self._Integer = None # Token
            self.s1 = None # Field_segmentContext
            self.s2 = None # Index_segmentContext

        def OBracket(self):
            return self.getToken(FieldPathPythonParser.OBracket, 0)

        def CBracket(self):
            return self.getToken(FieldPathPythonParser.CBracket, 0)

        def Integer(self):
            return self.getToken(FieldPathPythonParser.Integer, 0)

        def index_segment(self):
            return self.getTypedRuleContext(FieldPathPythonParser.Index_segmentContext,0)


        def Period(self):
            return self.getToken(FieldPathPythonParser.Period, 0)

        def field_segment(self):
            return self.getTypedRuleContext(FieldPathPythonParser.Field_segmentContext,0)


        def getRuleIndex(self):
            return FieldPathPythonParser.RULE_index_segment

        def enterRule(self, listener):
            if hasattr(listener, "enterIndex_segment"):
                listener.enterIndex_segment(self)

        def exitRule(self, listener):
            if hasattr(listener, "exitIndex_segment"):
                listener.exitIndex_segment(self)




    def index_segment(self):

        localctx = FieldPathPythonParser.Index_segmentContext(self, self._ctx, self.state)
        self.enterRule(localctx, 6, self.RULE_index_segment)
        self._la = 0 # Token type
        try:
            self.enterOuterAlt(localctx, 1)
            self.state = 37
            self.match(FieldPathPythonParser.OBracket)
            self.state = 39
            self._errHandler.sync(self)
            _la = self._input.LA(1)
            if _la==FieldPathPythonParser.Integer:
                self.state = 38
                localctx._Integer = self.match(FieldPathPythonParser.Integer)


            self.state = 41
            self.match(FieldPathPythonParser.CBracket)
            self.state = 45
            self._errHandler.sync(self)
            token = self._input.LA(1)
            if token in [FieldPathPythonParser.Period]:
                self.state = 42
                self.match(FieldPathPythonParser.Period)
                self.state = 43
                localctx.s1 = self.field_segment()
                pass
            elif token in [FieldPathPythonParser.OBracket]:
                self.state = 44
                localctx.s2 = self.index_segment()
                pass
            elif token in [FieldPathPythonParser.EOF]:
                pass
            else:
                pass

            localctx.seg = IndexSegment(
                    index=(None if localctx._Integer is None else localctx._Integer.text),
                    child=(None if (None if localctx.s1 is None else localctx.s1.start) is None and (None if localctx.s2 is None else localctx.s2.start) is None else localctx.s2.seg if (None if localctx.s1 is None else localctx.s1.start) is None and (None if localctx.s2 is None else localctx.s2.start) is not None else localctx.s1.seg))

        except RecognitionException as re:
            localctx.exception = re
            self._errHandler.reportError(self, re)
            self._errHandler.recover(self, re)
        finally:
            self.exitRule()
        return localctx





