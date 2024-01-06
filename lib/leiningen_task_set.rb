require 'rake_factory'
require 'rake_leiningen'

# Taken from https://github.com/infrablocks/rake_leiningen/blob/master/lib/rake_leiningen/task_sets/checks.rb
# Modified here to remove eastwood and cljfmt (lint and format respectively)

class Checks < RakeFactory::TaskSet
  parameter :argument_names, default: []

  parameter :profile
  parameter :environment
  parameter :directory, default: '.'
  parameter :fix, default: false

  parameter :ensure_task_name, default: 'leiningen:ensure'

  parameter :pedantise_task_name, :default => :pedantise
  parameter :check_task_name, :default => :check

  task RakeLeiningen::Tasks::Pedantise,
       name: RakeFactory::DynamicValue.new { |ts| ts.pedantise_task_name }
  task RakeLeiningen::Tasks::Check,
       name: RakeFactory::DynamicValue.new { |ts| ts.check_task_name }
end

def self.define_check_tasks(opts = {}, &block)
  Checks.define(opts, &block)
end
