//References:
1. http://cobb.typepad.com/files/mdx.pdf

//Simple
SELECT {[Date.Y-M-D].[2017].[2].CHILDREN} ON COLUMNS,
Crossjoin([Category].[Name].MEMBERS,
          [Difficulty].[Name].MEMBERS) ON ROWS
FROM [ProblemAttempt].[Fact Count]


//Sub-measures
WITH
MEMBER [Measures].[Score] AS
'([Measures].[Correct] / [Measures].[Fact Count])*100'
SELECT Crossjoin({[Date.Y-M-D].[2017].[2].CHILDREN},
 {[Measures].[Score],[Measures].[Correct],[Measures].[Fact Count]}) ON COLUMNS,
[Category].[Name].MEMBERS ON ROWS
FROM [ProblemAttempt]
