# Graph Report - D:\claude_program\Agentx\Agentx_learning\reference\AgentX\AgentX\src\main\java\org\xhy\application\auth  (2026-04-18)

## Corpus Check
- 6 files · ~870 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 63 nodes · 83 edges · 5 communities detected
- Extraction: 70% EXTRACTED · 30% INFERRED · 0% AMBIGUOUS · INFERRED: 25 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Community Hubs (Navigation)
- [[_COMMUNITY_Community 0|Community 0]]
- [[_COMMUNITY_Community 1|Community 1]]
- [[_COMMUNITY_Community 2|Community 2]]
- [[_COMMUNITY_Community 3|Community 3]]
- [[_COMMUNITY_Community 4|Community 4]]

## God Nodes (most connected - your core abstractions)
1. `AuthSettingDTO` - 21 edges
2. `UpdateAuthSettingRequest` - 11 edges
3. `AuthSettingAppService` - 9 edges
4. `LoginMethodDTO` - 7 edges
5. `AuthConfigDTO` - 5 edges
6. `AuthSettingAssembler` - 4 edges

## Surprising Connections (you probably didn't know these)
- None detected - all connections are within the same source files.

## Communities

### Community 0 - "Community 0"
Cohesion: 0.1
Nodes (1): AuthSettingDTO

### Community 1 - "Community 1"
Cohesion: 0.16
Nodes (2): AuthSettingAppService, AuthSettingAssembler

### Community 2 - "Community 2"
Cohesion: 0.27
Nodes (1): UpdateAuthSettingRequest

### Community 3 - "Community 3"
Cohesion: 0.25
Nodes (1): LoginMethodDTO

### Community 4 - "Community 4"
Cohesion: 0.33
Nodes (1): AuthConfigDTO

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `AuthSettingDTO` connect `Community 0` to `Community 2`?**
  _High betweenness centrality (0.545) - this node is a cross-community bridge._
- **Why does `AuthSettingAppService` connect `Community 1` to `Community 2`?**
  _High betweenness centrality (0.230) - this node is a cross-community bridge._
- **Should `Community 0` be split into smaller, more focused modules?**
  _Cohesion score 0.1 - nodes in this community are weakly interconnected._