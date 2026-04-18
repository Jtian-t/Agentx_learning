# Graph Report - D:\claude_program\Agentx\Agentx_learning  (2026-04-18)

## Corpus Check
- 6 files · ~9,264 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 48 nodes · 42 edges · 6 communities detected
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Community 0|Community 0]]
- [[_COMMUNITY_Community 1|Community 1]]
- [[_COMMUNITY_Community 2|Community 2]]
- [[_COMMUNITY_Community 3|Community 3]]
- [[_COMMUNITY_Community 4|Community 4]]
- [[_COMMUNITY_Community 5|Community 5]]

## God Nodes (most connected - your core abstractions)
1. `UserEntity` - 23 edges
2. `BaseEntity` - 11 edges
3. `BusinessException` - 3 edges
4. `AgentXLearningApplication` - 2 edges
5. `AgentXLearningApplicationTests` - 2 edges

## Surprising Connections (you probably didn't know these)
- None detected - all connections are within the same source files.

## Communities

### Community 0 - "Community 0"
Cohesion: 0.08
Nodes (1): UserEntity

### Community 1 - "Community 1"
Cohesion: 0.17
Nodes (1): BaseEntity

### Community 2 - "Community 2"
Cohesion: 0.5
Nodes (1): BusinessException

### Community 3 - "Community 3"
Cohesion: 0.67
Nodes (1): AgentXLearningApplication

### Community 4 - "Community 4"
Cohesion: 0.67
Nodes (1): AgentXLearningApplicationTests

### Community 5 - "Community 5"
Cohesion: 1.0
Nodes (0): 

## Knowledge Gaps
- **Thin community `Community 5`** (2 nodes): `Operator.java`, `needCheckUserId()`
  Too small to be a meaningful cluster - may be noise or needs more connections extracted.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.08 - nodes in this community are weakly interconnected._