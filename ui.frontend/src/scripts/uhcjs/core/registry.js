class Registry {
	constructor() {
		this.registry = {};
	}

	getRegistry() {
		return this.registry;
	}

	getRegisteredComponentTypes() {
		return Object.keys(this.registry);
	}

	registerComponent(module) {
		if (this.registry[module.name]) {
			console.log(`Component with ${module.name} selector already exists.`);
		}
		this.registry[module.name] = module;
	}
}

export default new Registry();
