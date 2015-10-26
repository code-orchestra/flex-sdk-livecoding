package org.apache.flex.forks.velocity.runtime.parser.node;

/*
 * Copyright 2000-2001,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public interface ParserVisitor
{
  Object visit(SimpleNode node, Object data);
  Object visit(ASTprocess node, Object data);
  Object visit(ASTComment node, Object data);
  Object visit(ASTNumberLiteral node, Object data);
  Object visit(ASTStringLiteral node, Object data);
  Object visit(ASTIdentifier node, Object data);
  Object visit(ASTWord node, Object data);
  Object visit(ASTDirective node, Object data);
  Object visit(ASTBlock node, Object data);
  Object visit(ASTObjectArray node, Object data);
  Object visit(ASTMethod node, Object data);
  Object visit(ASTReference node, Object data);
  Object visit(ASTTrue node, Object data);
  Object visit(ASTFalse node, Object data);
  Object visit(ASTText node, Object data);
  Object visit(ASTIfStatement node, Object data);
  Object visit(ASTElseStatement node, Object data);
  Object visit(ASTElseIfStatement node, Object data);
  Object visit(ASTSetDirective node, Object data);
  Object visit(ASTExpression node, Object data);
  Object visit(ASTAssignment node, Object data);
  Object visit(ASTOrNode node, Object data);
  Object visit(ASTAndNode node, Object data);
  Object visit(ASTEQNode node, Object data);
  Object visit(ASTNENode node, Object data);
  Object visit(ASTLTNode node, Object data);
  Object visit(ASTGTNode node, Object data);
  Object visit(ASTLENode node, Object data);
  Object visit(ASTGENode node, Object data);
  Object visit(ASTAddNode node, Object data);
  Object visit(ASTSubtractNode node, Object data);
  Object visit(ASTMulNode node, Object data);
  Object visit(ASTDivNode node, Object data);
  Object visit(ASTModNode node, Object data);
  Object visit(ASTNotNode node, Object data);
}
