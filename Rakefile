# frozen_string_literal: true

require 'rake_leiningen'
require 'rake_terraform'
require 'rake_vault'
require 'vault'
require 'yaml'
require 'rake_fly'
require 'uri'

require_relative 'lib/leiningen_task_set'

task default: %i[library:check library:test:unit]

RakeFly.define_installation_tasks(version: '7.9.0')

RakeLeiningen.define_installation_tasks(
  version: '2.10.0'
)

RakeVault.define_installation_tasks(
  path: File.join(Dir.pwd, 'vendor', 'vault'),
  version: '1.11.2'
)

namespace :vault do
  RakeVault.define_login_task(
    argument_names: [:role, :address],
  ) do |t, args|
    t.address = args[:address]
    t.role = args[:role] || 'read-only'
  end
end

namespace :library do
  define_check_tasks(fix: true)

  namespace :test do
    RakeLeiningen.define_test_task(
      name: :unit,
      type: 'unit',
      profile: 'test')
  end

  namespace :publish do
    RakeLeiningen.define_release_task(
      name: :release,
      profile: 'release')  do |t|
          t.environment = {
              'VERSION' => ENV['VERSION'],
              'CLOJARS_DEPLOY_USERNAME' => ENV['CLOJARS_DEPLOY_USERNAME'],
              'CLOJARS_DEPLOY_TOKEN' => ENV['CLOJARS_DEPLOY_TOKEN']
          }
          end
  end

  desc 'Lint all src files'
  task :lint do
    puts "Running clj-kondo from ./scripts/lint for " + RUBY_PLATFORM
    platform_prefix = /darwin/ =~ RUBY_PLATFORM ? "mac" : "linux"

    sh("./scripts/lint/clj-kondo-2021-06-18-#{platform_prefix}",
       "--lint", "src/")

    puts "Finished running clj-kondo"
  end

  desc 'Reformat all src files'
  task :format do
    puts "Running cljstyle from ./scripts/lint for " + RUBY_PLATFORM
    platform_prefix = /darwin/ =~ RUBY_PLATFORM ? "mac" : "linux"

    sh("./scripts/lint/cljstyle-0-15-0-#{platform_prefix}", "fix")

    puts "Finished running cljstyle"
  end

  task :optimise do
    puts 'skipping optimise...'
  end
  task :idiomise do
    puts 'skipping idiomise...'
  end
end

namespace :template do
  desc 'Populate this template based on the provided parameters.'
  task :populate, [:vault_address, :library_name] do |t, args|
    Rake::Task[:'vault:login'].invoke('kv-admin', args[:vault_address])

    library_name = args.library_name.downcase
    vault_address = args.vault_address

    Rake::Task[:'template:vault'].invoke(vault_address, library_name)

    directory_location = "../#{library_name}"
    snakecased_name = library_name.underscore

    if File.exists?(directory_location)
      puts "ERROR: Target directory already exists. Exiting."
      exit 1
    end

    puts
    puts "Copying to target directory."
    mkdir_p(directory_location)
    cp_r("./", directory_location)

    puts
    puts "Removing old .git directory"
    remove_dir(directory_location + "/.git", force = false)

    puts
    puts "Applying replacements to file contents."
    Dir["#{directory_location}/**/*"].each do |path|
      next if File.directory?(path)
      contents = File.read(path)

      contents = contents.gsub("zebra", library_name)

      File.open(path, 'w') { |f| f.write(contents) }
    end

    puts
    puts "Renaming directories."
    Dir["#{directory_location}/**/*"].each do |path|
      if File.directory?(path) && path.match?(/library_template$/)
        mv(path, path.gsub(/library_template$/, snakecased_name))
      end
    end

    puts
    puts "Setting up git."

    Rake::Task[:'template:git'].invoke

    puts
    puts "Done."
    puts "Now push to CI manually."
  end

  desc 'Copy vault secrets from the library template'
  task :vault, [:vault_address, :library_name] do |t, args|
      token = File.read(File.expand_path('~/.vault-token'))
      puts args.vault_address

      vault_client = Vault::Client.new(address: args.vault_address, token: token)
      template_base_path = 'zebra'

      library_name = args.library_name.downcase

      puts 'copying value for clojars_deploy_username from zebra'
      clojars_deploy_username = vault_client.kv('kv').read("#{template_base_path}/clojars_deploy_username")
      vault_client.kv('kv').write("#{library_name}/clojars_deploy_username", value: clojars_deploy_username)

      puts 'copying value for clojars_deploy_token from zebra'
      clojars_deploy_token = vault_client.kv('kv').read("#{template_base_path}/clojars_deploy_token")
      vault_client.kv('kv').write("#{library_name}/clojars_deploy_token", value: clojars_deploy_token)

      puts 'copying value for application_builder_image_repository_url from zebra'
      application_builder_image_repository_url = vault_client.kv('kv').read("#{template_base_path}/application_builder_image_repository_url")
      vault_client.kv('kv').write("#{library_name}/application_builder_image_repository_url", value: application_builder_image_repository_url)

      puts 'copying value for application_builder_image_tag from zebra'
      application_builder_image_tag = vault_client.kv('kv').read("#{template_base_path}/application_builder_image_tag")
      vault_client.kv('kv').write("#{library_name}/application_builder_image_tag", value: application_builder_image_tag)

      puts 'copying value for slack_failure_channel from zebra'
      slack_failure_channel = vault_client.kv('kv').read("#{template_base_path}/slack_failure_channel")
      vault_client.kv('kv').write("#{library_name}/slack_failure_channel", value: slack_failure_channel)

      puts 'copying value for slack_failure_message from zebra'
      slack_failure_message = vault_client.kv('kv').read("#{template_base_path}/slack_failure_message")
      vault_client.kv('kv').write("#{library_name}/slack_failure_message", value: slack_failure_message)

      puts 'copying value for slack_success_channel from zebra'
      slack_success_channel = vault_client.kv('kv').read("#{template_base_path}/slack_success_channel")
      vault_client.kv('kv').write("#{library_name}/slack_success_channel", value: slack_success_channel)

      puts 'copying value for slack_success_message from zebra'
      slack_success_message = vault_client.kv('kv').read("#{template_base_path}/slack_success_message")
      vault_client.kv('kv').write("#{library_name}/slack_success_message", value: slack_success_message)

      puts 'copied vault secrets'
  end

  desc 'Initialises git'
  task :git do
    puts "Initialising Git..."
    `./scripts/init/git.sh`
    puts "Done."
    puts
  end

end

namespace :ci do
  RakeFly.define_project_tasks(
    pipeline: 'zebra',
    argument_names: [:concourse_url],
    backend: RakeFly::Tasks::Authentication::Login::FlyBackend
  ) do |t, args|

    t.concourse_url = args[:concourse_url]
    t.config = "pipelines/pipeline.yaml"
    t.non_interactive = true
    t.home_directory = 'build/fly'
  end
end
