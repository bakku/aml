# Run with $JAVA_HOME/bin/ruby --jvm --polyglot polyglot.rb

puts Polyglot.eval('aml', 'π;').to_f

if Polyglot.eval('aml', '∀(x ∈ {2, 4, 6}: x mod 2 = 0);')
  puts "True"
else
  puts "False"
end

puts Polyglot.eval('aml', '1/3 · 3;').to_i

result = Polyglot.eval('aml', '{1, ..., 5};')
puts result[2].to_i

func = Polyglot.eval('aml', 'f: (x) → x + 1;')
puts func.(1).to_i

evenFunc = Polyglot.eval('aml', 'even: (S) → ∀(x ∈ S: x mod 2 = 0);')
puts evenFunc.([1, 2, 3]) == true
