function cov_24hl12gde2() {
  var path = "D:\\WORKDIR\\OC\\p5\\Test-Full-Stack-App\\front\\src\\app\\learning\\sum.js";
  var hash = "6cb3e5687418857cd7d6baceee59a74a276ff248";
  var global = new Function("return this")();
  var gcv = "__coverage__";
  var coverageData = {
    path: "D:\\WORKDIR\\OC\\p5\\Test-Full-Stack-App\\front\\src\\app\\learning\\sum.js",
    statementMap: {
      "0": {
        start: {
          line: 2,
          column: 4
        },
        end: {
          line: 2,
          column: 17
        }
      },
      "1": {
        start: {
          line: 4,
          column: 0
        },
        end: {
          line: 4,
          column: 21
        }
      }
    },
    fnMap: {
      "0": {
        name: "sum",
        decl: {
          start: {
            line: 1,
            column: 9
          },
          end: {
            line: 1,
            column: 12
          }
        },
        loc: {
          start: {
            line: 1,
            column: 19
          },
          end: {
            line: 3,
            column: 3
          }
        },
        line: 1
      }
    },
    branchMap: {},
    s: {
      "0": 0,
      "1": 0
    },
    f: {
      "0": 0
    },
    b: {},
    _coverageSchema: "1a1c01bbd47fc00a2c39e90264f33305004495a9",
    hash: "6cb3e5687418857cd7d6baceee59a74a276ff248"
  };
  var coverage = global[gcv] || (global[gcv] = {});

  if (!coverage[path] || coverage[path].hash !== hash) {
    coverage[path] = coverageData;
  }

  var actualCoverage = coverage[path];
  {
    // @ts-ignore
    cov_24hl12gde2 = function () {
      return actualCoverage;
    };
  }
  return actualCoverage;
}

cov_24hl12gde2();

function sum(a, b) {
  cov_24hl12gde2().f[0]++;
  cov_24hl12gde2().s[0]++;
  return a + b;
}

cov_24hl12gde2().s[1]++;
module.exports = sum;
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJuYW1lcyI6WyJzdW0iLCJhIiwiYiIsIm1vZHVsZSIsImV4cG9ydHMiXSwic291cmNlcyI6WyJzdW0uanMiXSwic291cmNlc0NvbnRlbnQiOlsiZnVuY3Rpb24gc3VtKGEsIGIpIHtcclxuICAgIHJldHVybiBhICsgYjtcclxuICB9XHJcbm1vZHVsZS5leHBvcnRzID0gc3VtOyJdLCJtYXBwaW5ncyI6Ijs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7O0lBZVk7Ozs7Ozs7Ozs7QUFmWixTQUFTQSxHQUFULENBQWFDLENBQWIsRUFBZ0JDLENBQWhCLEVBQW1CO0VBQUE7RUFBQTtFQUNmLE9BQU9ELENBQUMsR0FBR0MsQ0FBWDtBQUNEOzs7QUFDSEMsTUFBTSxDQUFDQyxPQUFQLEdBQWlCSixHQUFqQiJ9